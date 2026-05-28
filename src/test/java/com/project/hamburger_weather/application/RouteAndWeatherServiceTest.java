package com.project.hamburger_weather.application;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.project.hamburger_weather.domain.model.Address;
import com.project.hamburger_weather.domain.model.Coordinate;
import com.project.hamburger_weather.domain.model.ForecastReport;
import com.project.hamburger_weather.domain.model.HourlyForecast;
import com.project.hamburger_weather.domain.model.LocationForecast;
import com.project.hamburger_weather.domain.model.SavedRoute;
import com.project.hamburger_weather.domain.service.WeatherAnalysisService;
import com.project.hamburger_weather.exception.TagNotFoundException;
import com.project.hamburger_weather.infra.api.WeatherService;
import com.project.hamburger_weather.domain.service.AccidentAnalysisService;
import com.project.hamburger_weather.domain.model.AccidentReport;
import com.project.hamburger_weather.domain.model.RiskLevel;
import com.project.hamburger_weather.domain.service.CorrelateAccidentsAndWeatherService;
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

    @Mock
    private AccidentAnalysisService accidentAnalysisService;

    // test data
    private final Address from = new Address("Musterstraße", "1", "20095", "Hamburg", "Germany");
    private final Address to = new Address("Reeperbahn", "1", "20359", "Hamburg", "Germany");
    private final Coordinate fromCoord = new Coordinate(53.55, 10.0);
    private final Coordinate toCoord = new Coordinate(53.56, 10.1);
    private final List<Coordinate> coordinates = List.of(fromCoord, toCoord);
    private final LocationForecast locationForecast = buildTestLocationForecast();
    private final ForecastReport forecastReport = buildTestForecastReport();
    private final SavedRoute savedRoute = new SavedRoute("Test Tag 1", from, to, coordinates, LocalDateTime.now());
    private final AccidentReport accidentReport = buildTestAccidentReport();
    private final int correlatedAccidents = buildTestCorrelatedAccidents();

    @Test
    void shouldReturnForecastForGivenStartAndFinish() {

        when(savedRouteService.getRouteByAddress(
                any(Address.class), any(Address.class)))
                .thenReturn(Mono.just(savedRoute));

        when(weatherService.getForecast(any(Coordinate.class))).thenReturn(Mono.just(locationForecast));
        when(weatherAnalysisService.summarizeToReport(anyList())).thenReturn(forecastReport);
        when(accidentAnalysisService.getRoadAccidentsAssessment(anyList())).thenReturn(accidentReport);

        try (MockedStatic<CorrelateAccidentsAndWeatherService> mockedStatic
                = mockStatic(CorrelateAccidentsAndWeatherService.class)) {

            mockedStatic.when(()
                    -> CorrelateAccidentsAndWeatherService.correlateWeatherAndAccidents(
                            any(ForecastReport.class),
                            any(AccidentReport.class)
                    ))
                    .thenReturn(correlatedAccidents);

            StepVerifier.create(routeAndWeatherService.getFullAnalysisForStartAndFinish(from, to))
                    .expectNextMatches(result
                            -> result.route().size() == 2
                    && result.forecast() != null
                    )
                    .verifyComplete();
        }
    }

    @Test
    void shouldReturnForecastForGivenTag() {

        when(savedRouteService.getRouteByTag(
                any(String.class)))
                .thenReturn(Mono.just(savedRoute));

        when(weatherService.getForecast(any(Coordinate.class))).thenReturn(Mono.just(locationForecast));
        when(weatherAnalysisService.summarizeToReport(anyList())).thenReturn(forecastReport);
        when(accidentAnalysisService.getRoadAccidentsAssessment(anyList())).thenReturn(buildTestAccidentReport());

        try (MockedStatic<CorrelateAccidentsAndWeatherService> mockedStatic
                = mockStatic(CorrelateAccidentsAndWeatherService.class)) {

            mockedStatic.when(()
                    -> CorrelateAccidentsAndWeatherService.correlateWeatherAndAccidents(
                            any(ForecastReport.class),
                            any(AccidentReport.class)
                    ))
                    .thenReturn(correlatedAccidents);
            StepVerifier.create(routeAndWeatherService.getRouteForecastResultForGivenTag("Test Tag 1"))
                    .expectNextMatches(result
                            -> result.route().size() == 2
                    && result.forecast() != null
                    )
                    .verifyComplete();
        }
    }

    @Test
    void shouldReturnErrorWhenRoutingServiceIsNotResponding() {
        when(savedRouteService.getRouteByAddress(any(Address.class), any(Address.class)))
                .thenReturn(Mono.error(new RuntimeException("Error while fetching route from routing service")));

        StepVerifier.create(routeAndWeatherService.getFullAnalysisForStartAndFinish(from, to))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException
                && throwable.getMessage().equals("Error while fetching route from routing service"))
                .verify();

        verify(weatherService, never()).getForecast(any(Coordinate.class));
        verify(weatherAnalysisService, never()).summarizeToReport(anyList());
    }

    @Test
    void shouldReturnErrorWhenRouteNotFoundForGivenTag() {
        when(savedRouteService.getRouteByTag(any(String.class)))
                .thenReturn(Mono.error(new TagNotFoundException("Route with tag NonExistingTag not found")));

        StepVerifier.create(routeAndWeatherService.getRouteForecastResultForGivenTag("NonExistingTag"))
                .expectErrorMatches(throwable -> throwable instanceof TagNotFoundException
                && throwable.getMessage().equals("Route with tag NonExistingTag not found"))
                .verify();

        verify(weatherService, never()).getForecast(any(Coordinate.class));
        verify(weatherAnalysisService, never()).summarizeToReport(anyList());
    }

    private LocationForecast buildTestLocationForecast() {
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

    private ForecastReport buildTestForecastReport() {
        return new ForecastReport(
                20.0,
                20.0,
                20.0,
                0.0,
                false,
                true,
                false,
                false
        );
    }

    private AccidentReport buildTestAccidentReport() {
        return new AccidentReport(10, 5, 1, 0, 0, 0, 0, RiskLevel.HIGH);
    }

    private int buildTestCorrelatedAccidents() {
        return 5;
    }
}
