package com.project.hamburger_weather.application;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.project.hamburger_weather.domain.model.Address;
import com.project.hamburger_weather.domain.model.Coordinate;
import com.project.hamburger_weather.domain.model.ForecastReport;
import com.project.hamburger_weather.domain.model.HourlyForecast;
import com.project.hamburger_weather.domain.model.LocationForecast;
import com.project.hamburger_weather.domain.model.Route;
import com.project.hamburger_weather.domain.model.SavedRoute;
import com.project.hamburger_weather.domain.service.WeatherAnalysisService;
import com.project.hamburger_weather.exception.TagNotFoundException;
import com.project.hamburger_weather.infra.api.WeatherService;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
public class RouteAndWeatherServiceTest {

    @Mock
    private WeatherService weatherService;

    @Mock
    private SavedRouteService savedRouteService;

    @Mock
    private WeatherAnalysisService weatherAnalysisService;

    @InjectMocks
    private RouteAndWeatherService routeAndWeatherService;

    // test data
    private final Address from = new Address("Musterstraße", "1", "20095", "Hamburg", "Germany");
    private final Address to = new Address("Reeperbahn", "1", "20359", "Hamburg", "Germany");
    private final Coordinate fromCoord = new Coordinate(53.55, 10.0);
    private final Coordinate toCoord = new Coordinate(53.56, 10.1);
    private final List<Coordinate> coordinates = List.of(fromCoord, toCoord);
    private final Route route = new Route(coordinates);
    private final LocationForecast forecast = buildTestForecast();
    private final ForecastReport report = buildTestReport();
    private final SavedRoute savedRoute = new SavedRoute("Test Tag 1", from, to, route, LocalDateTime.now());

    @Test
    void shouldReturnForecastForGivenStartAndFinish() {

        when(savedRouteService.getRouteByAddress(
                any(), any()))
                .thenReturn(Mono.just(savedRoute));

        when(weatherService.getForecast(any())).thenReturn(Mono.just(forecast));
        when(weatherAnalysisService.summarizeToReport(any())).thenReturn(report);

        StepVerifier.create(routeAndWeatherService.getRouteForecastResultForGivenStartAndFinish(from, to))
                .expectNextMatches(result
                        -> result.route().coordinates().size() == 2
                && result.forecast() != null
                )
                .verifyComplete();
    }

    @Test
    void shouldReturnForecastForGivenTag() {

        when(savedRouteService.getRouteByTag(
                any()))
                .thenReturn(Mono.just(savedRoute));

        when(weatherService.getForecast(any())).thenReturn(Mono.just(forecast));
        when(weatherAnalysisService.summarizeToReport(any())).thenReturn(report);

        StepVerifier.create(routeAndWeatherService.getRouteForecastResultForGivenTag("Test Tag 1"))
                .expectNextMatches(result
                        -> result.route().coordinates().size() == 2
                && result.forecast() != null
                )
                .verifyComplete();
    }

    @Test
    void shouldReturnErrorWhenRoutingServiceIsNotResponding() {
        when(savedRouteService.getRouteByAddress(any(), any()))
                .thenReturn(Mono.error(new RuntimeException("Error while fetching route from routing service")));

        StepVerifier.create(routeAndWeatherService.getRouteForecastResultForGivenStartAndFinish(from, to))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException
                && throwable.getMessage().equals("Error while fetching route from routing service"))
                .verify();

        verify(weatherService, never()).getForecast(any());
        verify(weatherAnalysisService, never()).summarizeToReport(any());
    }

    @Test
    void shouldReturnErrorWhenRouteNotFoundForGivenTag() {
        when(savedRouteService.getRouteByTag(any()))
                .thenReturn(Mono.error(new TagNotFoundException("Route with tag NonExistingTag not found")));

        StepVerifier.create(routeAndWeatherService.getRouteForecastResultForGivenTag("NonExistingTag"))
                .expectErrorMatches(throwable -> throwable instanceof TagNotFoundException
                && throwable.getMessage().equals("Route with tag NonExistingTag not found"))
                .verify();

        verify(weatherService, never()).getForecast(any());
        verify(weatherAnalysisService, never()).summarizeToReport(any());
    }

    private LocationForecast buildTestForecast() {
        return new LocationForecast(
                new Coordinate(1.0, 10.0),
                Arrays.asList(new HourlyForecast(
                        LocalDateTime.now(),
                        20.0,
                        10.0,
                        0.0,
                        5.0,
                        "1"
                ))
        );
    }

    private ForecastReport buildTestReport() {
        return new ForecastReport(
                20.0,
                10.0,
                0.0,
                5.0,
                false,
                true,
                false
        );
    }
}
