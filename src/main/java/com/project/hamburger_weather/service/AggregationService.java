package com.project.hamburger_weather.service;

import java.time.Duration;

import org.springframework.stereotype.Service;

import com.project.hamburger_weather.dto.AddressDto;
import com.project.hamburger_weather.dto.CoordinatesDto;
import com.project.hamburger_weather.dto.ReportDto;
import com.project.hamburger_weather.dto.RouteDto;
import com.project.hamburger_weather.dto.WeatherForecastDto;
import com.project.hamburger_weather.mapper.RawToWeatherDtoMapper;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AggregationService {
    private final WeatherService weatherService;
    private final RoutingService routingService;
    private final GeoConverterService geoConverterService;
    private final WeatherSummaryService weatherSummaryService;
    private final CoordinatesOptimizationService coordinatesOptimizationService;

    public AggregationService(WeatherService weatherService, RoutingService routingService, GeoConverterService geoConverterService, WeatherSummaryService weatherSummaryService, CoordinatesOptimizationService coordinatesOptimizationService) {
        this.weatherService = weatherService;
        this.routingService = routingService;
        this.geoConverterService = geoConverterService;
        this.weatherSummaryService = weatherSummaryService;
        this.coordinatesOptimizationService = coordinatesOptimizationService;
    }

    public Mono<ReportDto> getTheAnswer(AddressDto addressStart, AddressDto addressEnd) {
        // Implementation for aggregating data from different services

        Mono<RouteDto> routeMono = buildRoute(addressStart, addressEnd).cache();

        Mono<RouteDto> optimized = routeMono.map(route -> coordinatesOptimizationService.deduplicate(route.coordinates(), 3.0))
            .cache();

        Flux<WeatherForecastDto> allForecast = getAllForecasts(optimized).cache();
        Mono<ReportDto> report = weatherSummaryService.summarizeToReport(allForecast);
        return report;
    }


    public Mono<RouteDto> buildRoute(AddressDto addressStart, AddressDto addressEnd) {

        return geoConverterService.getCoordinates(addressStart.street(), addressStart.num(), addressStart.plz(), addressStart.city(), addressStart.country())
            .delayElement(Duration.ofSeconds(1))
            .zipWith(geoConverterService.getCoordinates(addressEnd.street(), addressEnd.num(), addressEnd.plz(), addressEnd.city(), addressEnd.country()))
            .flatMap(tuple -> {
                CoordinatesDto start = tuple.getT1();
                CoordinatesDto end = tuple.getT2();

                return routingService.getRoute(
                    start.lon(), start.lat(), 
                    end.lon(), end.lat()
                );
            });
    }

    public Flux<WeatherForecastDto> getAllForecasts(Mono<RouteDto> monoRoute) {
        // validation on some level
        Flux<WeatherForecastDto> allCoordinates = monoRoute
            .flatMapMany(route -> 
                Flux.fromIterable(route.coordinates()))
            .delayElements(Duration.ofSeconds(1))
            .flatMap(point -> 
                weatherService.getForecast(point.lon(), point.lat())
                .map(RawToWeatherDtoMapper::convert));
        
        return allCoordinates;
    }

     
}
