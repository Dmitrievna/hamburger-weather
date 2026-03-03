package com.project.hamburger_weather.application;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.hamburger_weather.domain.model.Address;
import com.project.hamburger_weather.domain.model.Coordinate;
import com.project.hamburger_weather.domain.model.LocationForecast;
import com.project.hamburger_weather.domain.model.Route;
import com.project.hamburger_weather.infra.api.GeoConverterService;
import com.project.hamburger_weather.infra.api.RoutingService;
import com.project.hamburger_weather.infra.api.WeatherService;
import com.project.hamburger_weather.domain.model.ForecastReport;
import com.project.hamburger_weather.domain.service.WeatherAnalysisService;
import com.project.hamburger_weather.infra.support.CoordinatesOptimizator;
import com.project.hamburger_weather.domain.model.RouteForecastResult;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

// rename to be more precise and to distinguish from other servvices
@Service
public class RouteAndWeatherService {

    private final WeatherService weatherService;
    private final RoutingService routingService;
    private final GeoConverterService geoConverterService;
    private final CoordinatesOptimizator coordinatesOptimizator;
    private final WeatherAnalysisService weatherAnalysisService;

    public RouteAndWeatherService(WeatherService weatherService, RoutingService routingService,
            GeoConverterService geoConverterService, CoordinatesOptimizator coordinatesOptimizator, WeatherAnalysisService weatherAnalysisService) {
        this.weatherService = weatherService;
        this.routingService = routingService;
        this.geoConverterService = geoConverterService;
        this.coordinatesOptimizator = coordinatesOptimizator;
        this.weatherAnalysisService = weatherAnalysisService;
    }

    // maybe rename both class and method
    public Mono<RouteForecastResult> getRouteForecastResultForGivenStartAndFinish(Address from, Address to) {

        // convert to coordinates
        Mono<Coordinate> fromCoordinate = geoConverterService.getCoordinate(from);
        Mono<Coordinate> toCoordinate = geoConverterService.getCoordinate(to);

        // calculate the route and optimize the coordinates list
        Mono<Route> route
                = Mono.zip(fromCoordinate, toCoordinate)
                        .flatMap(tuple -> routingService.getRoute(tuple.getT1(), tuple.getT2()))
                        .map(r -> coordinatesOptimizator.deduplicate(r));

        // get weather predictions for a given coordinates
        Mono<List<LocationForecast>> routeForecast
                = route.flatMap(c -> Flux.fromIterable(c.coordinates())
                .flatMap(coord -> weatherService.getForecast(coord))
                .collectList());

        // analyze the weather predictions and summarize to a report
        Mono<RouteForecastResult> result = Mono.zip(route, routeForecast)
                .map(tuple -> new RouteForecastResult(
                tuple.getT1(),
                weatherAnalysisService.summarizeToReport(tuple.getT2())
        ));

        return result;
    }
}
