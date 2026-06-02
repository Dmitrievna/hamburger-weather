package com.project.hamburger_weather.application;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.hamburger_weather.domain.model.Address;
import com.project.hamburger_weather.domain.model.WeatherReport;
import com.project.hamburger_weather.domain.model.LocationForecast;
import com.project.hamburger_weather.infra.api.GeoConverterService;
import com.project.hamburger_weather.infra.api.RoutingService;
import com.project.hamburger_weather.infra.api.WeatherService;
import com.project.hamburger_weather.domain.service.WeatherAnalysisService;
import com.project.hamburger_weather.domain.validation.RouteValidator;
import com.project.hamburger_weather.domain.model.RouteForecastResult;
import com.project.hamburger_weather.domain.model.SavedRoute;
import com.project.hamburger_weather.domain.service.AccidentAnalysisService;
import com.project.hamburger_weather.domain.model.AccidentReport;
import com.project.hamburger_weather.domain.service.CorrelateAccidentsAndWeatherService;
import com.project.hamburger_weather.infra.persistence.repository.RouteRequestRepository;
import com.project.hamburger_weather.infra.persistence.mapper.SavedRouteMapper;
import com.project.hamburger_weather.domain.model.Coordinate;
import com.project.hamburger_weather.infra.support.CoordinateOptimizer;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class WeatherAndAccidentService {

    private final WeatherService weatherService;
    private final RoutingService routingService;
    private final GeoConverterService geoConverterService;
    private final RouteResolutionService routeResolutionService;
    private final WeatherAnalysisService weatherAnalysisService;
    private final AccidentAnalysisService accidentAnalysisService;
    private final CorrelateAccidentsAndWeatherService correlateAccidentsAndWeatherService;
    private final RouteRequestRepository routeRequestRepository;
    private final SavedRouteMapper mapper;

    public WeatherAndAccidentService(WeatherService weatherService, RoutingService routingService,
            WeatherAnalysisService weatherAnalysisService, RouteResolutionService routeResolutionService, AccidentAnalysisService accidentAnalysisService, CorrelateAccidentsAndWeatherService correlateAccidentsAndWeatherService, GeoConverterService geoConverterService, RouteRequestRepository routeRequestRepository, SavedRouteMapper mapper) {
        this.weatherService = weatherService;
        this.weatherAnalysisService = weatherAnalysisService;
        this.routeResolutionService = routeResolutionService;
        this.routingService = routingService;
        this.geoConverterService = geoConverterService;
        this.accidentAnalysisService = accidentAnalysisService;
        this.correlateAccidentsAndWeatherService = correlateAccidentsAndWeatherService;
        this.routeRequestRepository = routeRequestRepository;
        this.mapper = mapper;
    }

    public Mono<RouteForecastResult> getForecast(Address from, Address to, String tag) {

        RouteValidator.validateRouteAddresses(from, to);

        // get the route for a given start and finish, either from db or from route api
        Mono<SavedRoute> route = routeResolutionService
                .getSavedRoute(from, to, tag)
                .switchIfEmpty(fetchAndSaveRoute(from, to, tag))
                .cache();

        return Mono.zip(route, route.flatMap(this::fetchWeather))
                .map(tuple -> buildForecastResult(tuple.getT1(), tuple.getT2()));

    }

    public Mono<SavedRoute> fetchAndSaveRoute(Address from, Address to, String tag) {
        Mono<Coordinate> fromCoordinate = geoConverterService.getCoordinate(from);
        Mono<Coordinate> toCoordinate = geoConverterService.getCoordinate(to);

        return Mono.zip(fromCoordinate, toCoordinate)
                .flatMap(tuple -> routingService.getRoute(tuple.getT1(), tuple.getT2()))
                .map(routeCoord -> CoordinateOptimizer.deduplicate(routeCoord))
                .flatMap(route -> {
                    String tagToSave = tag != null ? tag : generateTag(from, to);
                    return routeRequestRepository.save(mapper.toEntity(tagToSave, from, to, route))
                            .map(savedEntity -> {
                                return mapper.toDomain(savedEntity);

                            });

                });

    }

    private Mono<List<LocationForecast>> fetchWeather(SavedRoute route) {
        return Flux.fromIterable(route.coordinates())
                .flatMap(coord -> weatherService.getForecast(coord))
                .collectList();
    }

    private RouteForecastResult buildForecastResult(SavedRoute route, List<LocationForecast> weatherForecasts) {

        // get weather report
        WeatherReport weatherReport = weatherAnalysisService.summarizeToReport(weatherForecasts);
        // get accident report
        AccidentReport accidentReport = accidentAnalysisService.getRoadAccidentsAssessment(route.coordinates());

        // get correlation
        int correlatedAccidents = correlateAccidentsAndWeatherService.correlateWeatherAndAccidents(weatherReport, accidentReport);

        return new RouteForecastResult(
                route.coordinates(),
                weatherReport,
                correlatedAccidents,
                accidentReport.totalAccidents(),
                accidentReport.riskLevel());
    }

    private String generateTag(Address from, Address to) {
        return from.getStreet() + "-" + to.getStreet() + "-"
                + System.currentTimeMillis();
    }

}
