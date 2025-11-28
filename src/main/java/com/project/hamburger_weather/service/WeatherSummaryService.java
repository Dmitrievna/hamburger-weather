package com.project.hamburger_weather.service;

import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.function.Function;

import org.springframework.stereotype.Service;

import com.project.hamburger_weather.dto.HourlyForecastDto;
import com.project.hamburger_weather.dto.ReportDto;
import com.project.hamburger_weather.dto.WeatherForecastDto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;





@Service
public class WeatherSummaryService {
    private final WmoCodesService wmoCodesService;

    public WeatherSummaryService(WmoCodesService wmoCodesService) {
        this.wmoCodesService = wmoCodesService;
    }

    public Mono<ReportDto> summarizeToReport(Flux<WeatherForecastDto> forecastList) {

        Mono<List<WeatherForecastDto>> forecastToList = forecastList.collectList();

        Mono<DoubleSummaryStatistics> temperatureStats = forecastToList.map(list -> summarize(list, HourlyForecastDto::temperature));
        Mono<DoubleSummaryStatistics> precipationStats = forecastToList.map(list -> summarize(list, HourlyForecastDto::precipitation));
        Mono<DoubleSummaryStatistics> precipationProbStats = forecastToList.map(list -> summarize(list, HourlyForecastDto::precipitationProbability));
        Mono<DoubleSummaryStatistics> windSpeedStats = forecastToList.map(list -> summarize(list, HourlyForecastDto::windSpeed));
        Mono<List<String>> weatherCodes = forecastToList.map(list -> {
            List<String> codes = new ArrayList<>();
            list.forEach(item -> {
                item.hourlyForecast().forEach(hour -> {
                    codes.add(hour.weatherCode());});
                });
            return codes;
         });

        Mono<Boolean> rainy = Mono.zip(precipationStats, precipationProbStats, weatherCodes)
            .map(tuple -> {
                DoubleSummaryStatistics precipStats = tuple.getT1();
                DoubleSummaryStatistics precipProbStats = tuple.getT2();
                List<String> codes = tuple.getT3();
                Boolean rainByPrecip = precipStats.getMin() > 0.0;
                Boolean rainByProb = precipProbStats.getMin() > 30.0;
                Boolean rainByCode = codes.stream().anyMatch(code -> wmoCodesService.checkIfRain(code));
                return rainByPrecip || rainByProb || rainByCode;
            }); 
        
        
        

        Mono<Boolean> windy = windSpeedStats.map(wind -> wind.getMin() > 10.0);


        Mono<Boolean> warm = temperatureStats.map(temp -> temp.getAverage() > 12.0);
        
        Mono<Boolean> goodWeatherMono = Mono.zip(rainy, windy, warm, weatherCodes)
        .map(tuple -> {
            Boolean rn = tuple.getT1();
            Boolean wnd = tuple.getT2();
            Boolean wrm = tuple.getT3();
            List<String> cds = tuple.getT4();
            Boolean goodByCodes = cds.stream().allMatch(code -> wmoCodesService.checkIfGood(code));
            return wrm && !rn && !wnd && goodByCodes;
        });

        return Mono.zip(temperatureStats, precipationProbStats, rainy, windy, goodWeatherMono)
            .map(tuple -> {
                
                DoubleSummaryStatistics tmp = tuple.getT1(); 
                DoubleSummaryStatistics prcp = tuple.getT2(); 
                boolean rn = tuple.getT3(); 
                boolean wnd = tuple.getT4(); 
                boolean goodW = tuple.getT5();
                
                
                return new ReportDto(
                tmp.getAverage(),
                tmp.getMin(),
                tmp.getMax(),
                prcp.getAverage(),
                 rn,
                 goodW,
                 wnd
            );
        }).cast(ReportDto.class);
}



private DoubleSummaryStatistics summarize(
    List<WeatherForecastDto> list,
    Function<HourlyForecastDto, Double> valueExtractor
) {
    DoubleSummaryStatistics stats = new DoubleSummaryStatistics();
    list.forEach(item ->
        item.hourlyForecast().forEach(hour ->
            stats.accept(valueExtractor.apply(hour))
        )
    );
    return stats;
}

}
