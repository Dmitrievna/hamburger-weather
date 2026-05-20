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
import com.project.hamburger_weather.domain.service.WeatherAnalysisService;
import com.project.hamburger_weather.infra.support.CoordinatesOptimizator;
import com.project.hamburger_weather.domain.model.RouteForecastResult;
import com.project.hamburger_weather.infra.persistence.mapper.SavedRouteMapper;
import com.project.hamburger_weather.infra.persistence.repository.RouteRequestRepository;
import com.project.hamburger_weather.infra.persistence.entity.RouteEntity;
import com.project.hamburger_weather.domain.model.SavedRoute;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

// rename to be more precise and to distinguish from other servvices
@Service
public class RouteAndWeatherService {

    private final WeatherService weatherService;
    private final WeatherAnalysisService weatherAnalysisService;
    private final SavedRouteService savedRouteService;

    public RouteAndWeatherService(WeatherService weatherService, RoutingService routingService,
            GeoConverterService geoConverterService, CoordinatesOptimizator coordinatesOptimizator, WeatherAnalysisService weatherAnalysisService, SavedRouteService savedRouteService, RouteRequestRepository routeRequestRepository, SavedRouteMapper savedRouteMapper) {
        this.weatherService = weatherService;
        this.weatherAnalysisService = weatherAnalysisService;
        this.savedRouteService = savedRouteService;
    }

    // maybe rename both class and method
    public Mono<RouteForecastResult> getRouteForecastResultForGivenStartAndFinish(Address from, Address to) {

        Mono<SavedRoute> route = savedRouteService.getRouteByAddress(from, to);
        // get weather predictions for a given coordinates
        Mono<List<LocationForecast>> routeForecast
                = route.flatMap(r -> Flux.fromIterable(r.route().coordinates())
                .flatMap(coord -> weatherService.getForecast(coord))
                .collectList());

        // analyze the weather predictions and summarize to a report
        Mono<RouteForecastResult> result = Mono.zip(route, routeForecast)
                .map(tuple -> new RouteForecastResult(
                tuple.getT1().route(),
                weatherAnalysisService.summarizeToReport(tuple.getT2())
        ));

        return result;
    }

    public Mono<RouteForecastResult> getRouteForecastResultForGivenTag(String tag) {
        return savedRouteService.getRouteByTag(tag)
                .flatMap(savedRoute -> {
                    Route route = savedRoute.route();
                    Mono<List<LocationForecast>> routeForecast
                            = Flux.fromIterable(route.coordinates())
                                    .flatMap(coord -> weatherService.getForecast(coord))
                                    .collectList();

                    return routeForecast.map(forecasts -> new RouteForecastResult(
                            route,
                            weatherAnalysisService.summarizeToReport(forecasts)
                    ));
                });
    }
}
