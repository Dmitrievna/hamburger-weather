package com.project.hamburger_weather.application;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.hamburger_weather.domain.model.Address;
import com.project.hamburger_weather.domain.model.ForecastReport;
import com.project.hamburger_weather.domain.model.LocationForecast;
import com.project.hamburger_weather.infra.api.RoutingService;
import com.project.hamburger_weather.infra.api.WeatherService;
import com.project.hamburger_weather.domain.service.WeatherAnalysisService;
import com.project.hamburger_weather.domain.model.RouteForecastResult;
import com.project.hamburger_weather.domain.model.SavedRoute;
import com.project.hamburger_weather.domain.service.AccidentAnalysisService;
import com.project.hamburger_weather.domain.model.AccidentReport;
import com.project.hamburger_weather.domain.service.CorrelateAccidentsAndWeatherService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

// rename to be more precise and to distinguish from other servvices
@Service
public class RouteAndWeatherService {

    private final WeatherService weatherService;
    private final WeatherAnalysisService weatherAnalysisService;
    private final SavedRouteService savedRouteService;
    private final AccidentAnalysisService accidentAnalysisService;

    public RouteAndWeatherService(WeatherService weatherService, RoutingService routingService,
            WeatherAnalysisService weatherAnalysisService, SavedRouteService savedRouteService, AccidentAnalysisService accidentAnalysisService) {
        this.weatherService = weatherService;
        this.weatherAnalysisService = weatherAnalysisService;
        this.savedRouteService = savedRouteService;
        this.accidentAnalysisService = accidentAnalysisService;
    }

    // maybe rename both class and method
    public Mono<RouteForecastResult> getFullAnalysisForStartAndFinish(Address from, Address to) {

        // get the route for a given start and finish
        Mono<SavedRoute> savedRoute = savedRouteService.getRouteByAddress(from, to);
        // get weather predictions for a given coordinates
        Mono<List<LocationForecast>> routeForecast
                = savedRoute.flatMap(r -> Flux.fromIterable(r.coordinates())
                .flatMap(coord -> weatherService.getForecast(coord))
                .collectList());

        // analyze the weather predictions
        Mono<ForecastReport> forecastReport = routeForecast.map(forecasts -> weatherAnalysisService.summarizeToReport(forecasts));

        // analyze the accident risk for a given route
        Mono<AccidentReport> accidentReport = savedRoute.map(r -> accidentAnalysisService.getRoadAccidentsAssessment(r.coordinates()));
        // combine

        Mono<Integer> correlatedAccidents = Mono.zip(forecastReport, accidentReport)
                .map(tuple -> CorrelateAccidentsAndWeatherService.correlateWeatherAndAccidents(tuple.getT1(), tuple.getT2()));
        Mono<RouteForecastResult> result = Mono.zip(savedRoute, forecastReport, accidentReport, correlatedAccidents)
                .map(tuple -> new RouteForecastResult(
                tuple.getT1().coordinates(),
                tuple.getT2(),
                tuple.getT3().totalAccidents(),
                tuple.getT4(),
                tuple.getT3().riskLevel()
        ));
        return result;
    }

    public Mono<RouteForecastResult> getRouteForecastResultForGivenTag(String tag) {

        Mono<SavedRoute> route = savedRouteService.getRouteByTag(tag);

        Mono<List<LocationForecast>> locationForecasts = route
                .flatMap(r -> {
                    return Flux.fromIterable(r.coordinates())
                            .flatMap(c -> weatherService.getForecast(c))
                            .collectList();
                });
        Mono<ForecastReport> forecastReport = locationForecasts.map(forecasts -> weatherAnalysisService.summarizeToReport(forecasts));
        // analyze the accident risk for a given route
        Mono<AccidentReport> accidentReport = route.map(r -> accidentAnalysisService.getRoadAccidentsAssessment(r.coordinates()));
        // combine

        Mono<Integer> correlatedAccidents = Mono.zip(forecastReport, accidentReport)
                .map(tuple -> CorrelateAccidentsAndWeatherService.correlateWeatherAndAccidents(tuple.getT1(), tuple.getT2()));
        Mono<RouteForecastResult> result = Mono.zip(route, forecastReport, accidentReport, correlatedAccidents)
                .map(tuple -> new RouteForecastResult(
                tuple.getT1().coordinates(),
                tuple.getT2(),
                tuple.getT3().totalAccidents(),
                tuple.getT4(),
                tuple.getT3().riskLevel()
        ));
        return result;

    }

}
