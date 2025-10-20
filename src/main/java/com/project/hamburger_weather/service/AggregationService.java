package com.project.hamburger_weather.service;

import java.time.Duration;

import org.springframework.stereotype.Service;

import com.project.hamburger_weather.dto.AddressDto;
import com.project.hamburger_weather.dto.RouteDto;
import com.project.hamburger_weather.dto.WeatherForecastDto;
import com.project.hamburger_weather.mapper.RawToWeatherDtoMapper;
import com.project.hamburger_weather.dto.ReportDto;
import java.util.ArrayList;
import java.util.List;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuples;

@Service
public class AggregationService {
    private final WeatherService weatherService;
    private final RoutingService routingService;
    private final GeoConverterService geoConverterService;
    private final WeatherSummaryService weatherSummaryService;

    public AggregationService(WeatherService weatherService, RoutingService routingService, GeoConverterService geoConverterService, WeatherSummaryService weatherSummaryService) {
        this.weatherService = weatherService;
        this.routingService = routingService;
        this.geoConverterService = geoConverterService;
        this.weatherSummaryService = weatherSummaryService;
    }

    public Mono<ReportDto> getTheAnswer(AddressDto addressStart, AddressDto addressEnd) {
        // Implementation for aggregating data from different services

        Mono<RouteDto> routeMono = buildRoute(addressStart, addressEnd);
        Flux<WeatherForecastDto> allForecast = getAllForecasts(routeMono);
        Mono<ReportDto> report = weatherSummaryService.summarizeToReport(allForecast); 
        return report;
    }


    public Mono<RouteDto> buildRoute(AddressDto addressStart, AddressDto addressEnd) {

        Mono<RouteDto> routeMono =
        geoConverterService.getCoordinates(addressStart.street(), addressStart.num(), addressStart.plz(), addressStart.city(), addressStart.country())
            .flatMap(start -> Mono.delay(Duration.ofSeconds(1))
                                    .then(geoConverterService.getCoordinates(addressEnd.street(), addressEnd.num(), addressEnd.plz(), addressEnd.city(), addressEnd.country()))
                                    .map(end -> Tuples.of(start, end)))
                                    .flatMap(tuple -> {return routingService.getRoute(tuple.getT1().lon(), tuple.getT1().lat(), tuple.getT2().lon(), tuple.getT1().lat());
                                    });

     

        
        return routeMono;
    }

    public Flux<WeatherForecastDto> getAllForecasts(Mono<RouteDto> monoRoute) {
        // validation on some level
        Flux<WeatherForecastDto> allCoordinates = monoRoute
            .flatMapMany(route -> {
                return Flux.fromIterable(route.coordinates());
        })
            .flatMap(point -> {
                return weatherService.getForecast(point.lon(), point.lat())
                    .map(raw -> RawToWeatherDtoMapper.convert(raw));
        });
        
        return allCoordinates;
    }

     
}
