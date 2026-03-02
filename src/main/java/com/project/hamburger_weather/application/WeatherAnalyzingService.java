package com.project.hamburger_weather.application;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.hamburger_weather.domain.model.Address;
import com.project.hamburger_weather.domain.model.Coordinate;
import com.project.hamburger_weather.domain.model.LocationForecast;
import com.project.hamburger_weather.domain.model.Route;
import com.project.hamburger_weather.domain.util.CoordinatesOptimizator;
import com.project.hamburger_weather.infra.api.GeoConverterService;
import com.project.hamburger_weather.infra.api.RoutingService;
import com.project.hamburger_weather.infra.api.WeatherService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class WeatherAnalyzingService {

    private final WeatherService weatherService;
    private final RoutingService routingService;
    private final GeoConverterService geoConverterService;
    private final CoordinatesOptimizator coordinatesOptimizator;

    public WeatherAnalyzingService(WeatherService weatherService, RoutingService routingService,
            GeoConverterService geoConverterService, CoordinatesOptimizator coordinatesOptimizator) {
        this.weatherService = weatherService;
        this.routingService = routingService;
        this.geoConverterService = geoConverterService;
        this.coordinatesOptimizator = coordinatesOptimizator;
    }

    // maybe rename
    public Mono<List<LocationForecast>> analyzeWeatherForTheRoute(Address from, Address to) {

        Mono<Coordinate> fromCoordinate = geoConverterService.getCoordinate(from);
        Mono<Coordinate> toCoordinate = geoConverterService.getCoordinate(to);

        Mono<Route> route
                = Mono.zip(fromCoordinate, toCoordinate)
                        .flatMap(tuple -> routingService.getRoute(tuple.getT1(), tuple.getT2()))
                        .map(r -> coordinatesOptimizator.deduplicate(r));

        Mono<List<LocationForecast>> routeForecast
                = route.flatMap(c -> Flux.fromIterable(c.coordinates())
                .flatMap(coord -> weatherService.getForecast(coord))
                .collectList());

        return routeForecast;

    }
}
