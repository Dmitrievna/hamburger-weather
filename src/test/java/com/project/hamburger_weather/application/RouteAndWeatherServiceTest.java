package com.project.hamburger_weather.application;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.project.hamburger_weather.infra.persistence.entity.RouteEntity;
import com.project.hamburger_weather.infra.persistence.mapper.SavedRouteMapper;
import com.project.hamburger_weather.infra.persistence.repository.RouteRequestRepository;
import com.project.hamburger_weather.infra.support.CoordinatesOptimizator;
import reactor.core.publisher.Mono;
import com.project.hamburger_weather.domain.model.SavedRoute;
import com.project.hamburger_weather.domain.service.WeatherAnalysisService;
import com.project.hamburger_weather.infra.api.GeoConverterService;
import com.project.hamburger_weather.infra.api.RoutingService;
import com.project.hamburger_weather.infra.api.WeatherService;
import com.project.hamburger_weather.domain.model.Address;
import com.project.hamburger_weather.domain.model.Route;
import com.project.hamburger_weather.domain.model.Coordinate;
import java.util.Arrays;
import reactor.test.StepVerifier;
import com.project.hamburger_weather.domain.model.LocationForecast;
import com.project.hamburger_weather.domain.model.HourlyForecast;
import com.project.hamburger_weather.domain.model.ForecastReport;

@ExtendWith(MockitoExtension.class)
public class RouteAndWeatherServiceTest {

    @Mock
    private RouteRequestRepository repository;

    @Mock
    private RoutingService routingService;

    @Mock
    private GeoConverterService geoConverterService;

    @Mock
    private WeatherService weatherService;

    @Mock
    private SavedRouteMapper mapper;

    @Mock
    private CoordinatesOptimizator coordinatesOptimizator;

    @Mock
    private WeatherAnalysisService weatherAnalysisService;

    @InjectMocks
    private RouteAndWeatherService routeAndWeatherService;

    @Test
    void shouldReturnSavedRouteWhenItExistsInDatabase() {
        // given
        RouteEntity entity = buildRouteEntity();
        SavedRoute domain = buildRouteDomain();

        when(repository.existsByAddresses(any(), any(), any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(Mono.just(true));
        when(repository.findByAddresses(any(), any(), any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(Mono.just(entity));
        when(mapper.toDomain(entity))
                .thenReturn(domain);
        when(weatherService.getForecast(any()))
                .thenReturn(Mono.just(buildLocationForecast()));
        when(weatherAnalysisService.summarizeToReport(any()))
                .thenReturn(Mono.just(buildReport()));

        // when
        StepVerifier.create(routeAndWeatherService.getRouteForecastResultForGivenStartAndFinish(new Address("Test Street 1", "Test House Num 1", "Test PLZ 1", "Test City 1", "Test Country 1"), new Address("Test Street 2", "Test House Num 2", "Test PLZ 2", "Test City 2", "Test Country 2")))
                // then
                .expectNextMatches(result -> result.route().coordinates().equals(Arrays.asList(
                new Coordinate(1.0, 10.0))))
                .verifyComplete();

        // verify routing API was NOT called
        verify(routingService, never()).getRoute(any(), any());
    }

    private RouteEntity buildRouteEntity() {
        return new RouteEntity(
                1L,
                "Test Tag 1",
                "Test Street 1",
                "Test House Num 1",
                "Test PLZ 1",
                "Test City 1",
                "Test Country 1",
                "Test Street 2",
                "Test House Num 2",
                "Test PLZ 2",
                "Test City 2",
                "Test Country 2",
                "[{1.0, 10.0}]",
                LocalDateTime.now());
    }

    private SavedRoute buildRouteDomain() {
        return new SavedRoute(
                "Test Tag 1",
                new Address(
                        "Test Street 1",
                        "Test House Num 1",
                        "Test PLZ 1",
                        "Test City 1",
                        "Test Country 1"),
                new Address(
                        "Test Street 2",
                        "Test House Num 2",
                        "Test PLZ 2",
                        "Test City 2",
                        "Test Country 2"),
                new Route(
                        Arrays.asList(
                                new Coordinate(1.0, 10.0)
                        )),
                LocalDateTime.now());
    }

    private LocationForecast buildLocationForecast() {
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

    private ForecastReport buildReport() {
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
