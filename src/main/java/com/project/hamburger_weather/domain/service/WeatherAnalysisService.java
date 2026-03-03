package com.project.hamburger_weather.domain.service;

import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.project.hamburger_weather.domain.model.ForecastReport;
import com.project.hamburger_weather.domain.model.HourlyForecast;
import com.project.hamburger_weather.domain.model.LocationForecast;

@Service
public class WeatherAnalysisService {

    private final WmoCodesEvaluationService wmoCodesService;

    public WeatherAnalysisService(WmoCodesEvaluationService wmoCodesService) {
        this.wmoCodesService = wmoCodesService;
    }

    public ForecastReport summarizeToReport(List<LocationForecast> locationForecast) {

        List<HourlyForecast> allHourlyForecasts = locationForecast.stream()
                .flatMap(loc -> loc.hourlyForecasts().stream())
                .toList();

        DoubleSummaryStatistics temperatureStats = summarize(allHourlyForecasts, HourlyForecast::temperature);
        DoubleSummaryStatistics precipationStats = summarize(allHourlyForecasts, HourlyForecast::precipitation);
        DoubleSummaryStatistics precipationProbStats = summarize(allHourlyForecasts, HourlyForecast::precipitationProbability);
        DoubleSummaryStatistics windSpeedStats = summarize(allHourlyForecasts, HourlyForecast::windSpeed);

        List<String> weatherCodes = allHourlyForecasts.stream()
                .flatMap(hour -> hour.weatherCode() != null ? Stream.of(hour.weatherCode()) : Stream.empty())
                .toList();

        boolean rainByPrecip = precipationStats.getMin() > 0.0;
        boolean rainByProb = precipationProbStats.getMin() > 30.0;
        boolean rainByCode = weatherCodes.stream().anyMatch(code -> wmoCodesService.checkIfRain(code));
        boolean rainy = rainByPrecip || rainByProb || rainByCode;

        boolean windy = windSpeedStats.getMin() > 10.0;

        boolean warm = temperatureStats.getAverage() > 12.0;

        boolean goodByCodes = weatherCodes.stream().allMatch(code -> wmoCodesService.checkIfGood(code));

        boolean goodWeather = warm && !rainy && !windy && goodByCodes;

        return new ForecastReport(
                temperatureStats.getAverage(),
                temperatureStats.getMin(),
                temperatureStats.getMax(),
                precipationProbStats.getAverage(),
                rainy,
                goodWeather,
                windy);

    }

    private DoubleSummaryStatistics summarize(
            List<HourlyForecast> list,
            Function<HourlyForecast, Double> valueExtractor
    ) {
        DoubleSummaryStatistics stats = new DoubleSummaryStatistics();
        list.forEach(hour
                -> stats.accept(valueExtractor.apply(hour))
        );
        return stats;
    }

}
