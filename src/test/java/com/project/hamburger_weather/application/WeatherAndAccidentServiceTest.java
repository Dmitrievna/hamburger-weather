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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import org.mockito.junit.jupiter.MockitoExtension;

import com.project.hamburger_weather.domain.model.Address;
import com.project.hamburger_weather.domain.model.Coordinate;
import com.project.hamburger_weather.domain.model.WeatherReport;
import com.project.hamburger_weather.domain.model.HourlyForecast;
import com.project.hamburger_weather.domain.model.LocationForecast;
import com.project.hamburger_weather.domain.model.SavedRoute;
import com.project.hamburger_weather.domain.service.WeatherAnalysisService;
import com.project.hamburger_weather.infra.api.WeatherService;
import com.project.hamburger_weather.domain.service.AccidentAnalysisService;
import com.project.hamburger_weather.domain.model.AccidentReport;
import com.project.hamburger_weather.domain.model.RiskLevel;
import com.project.hamburger_weather.domain.service.CorrelateAccidentsAndWeatherService;
import com.project.hamburger_weather.infra.api.GeoConverterService;
import com.project.hamburger_weather.infra.api.RoutingService;
import com.project.hamburger_weather.infra.persistence.repository.RouteRequestRepository;
import com.project.hamburger_weather.infra.persistence.entity.RouteEntity;
import com.project.hamburger_weather.infra.persistence.mapper.SavedRouteMapper;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
public class WeatherAndAccidentServiceTest {

    @Mock
    private WeatherService weatherService;

    @Mock
    private RouteResolutionService routeResolutionService;

    @Mock
    private WeatherAnalysisService weatherAnalysisService;

    @Mock
    private CorrelateAccidentsAndWeatherService correlateAccidentsAndWeatherService;

    @InjectMocks
    private WeatherAndAccidentService weatherAndAccidentService;

    @Mock
    private AccidentAnalysisService accidentAnalysisService;

    @Mock
    private RoutingService routingService;

    @Mock
    private GeoConverterService geoConverterService;

    @Mock
    private RouteRequestRepository routeRequestRepository;

    @Mock
    private SavedRouteMapper mapper;

    // test data
    private final Address from = new Address("Musterstraße", "1", "20095", "Hamburg", "Germany");
    private final Address to = new Address("Reeperbahn", "1", "20359", "Hamburg", "Germany");
    private final Coordinate fromCoord = new Coordinate(53.55, 10.0);
    private final Coordinate toCoord = new Coordinate(53.56, 10.1);
    private final List<Coordinate> coordinates = List.of(fromCoord, toCoord);
    private final LocationForecast locationForecast = buildTestLocationForecast();
    private final WeatherReport forecastReport = buildTestForecastReport();
    private final SavedRoute savedRoute = new SavedRoute("Test Tag 1", from, to, coordinates, LocalDateTime.now());
    private final AccidentReport accidentReport = buildTestAccidentReport();
    private final int correlatedAccidents = buildTestCorrelatedAccidents();

    @Test
    void shouldReturnReportForTagAndAddressThatExist() {

        when(geoConverterService.getCoordinate(any(Address.class))).thenReturn(Mono.just(fromCoord));

        when(routeResolutionService.getSavedRoute(
                any(Address.class), any(Address.class), any(String.class)))
                .thenReturn(Mono.just(savedRoute));

        when(weatherService.getForecast(any(Coordinate.class))).thenReturn(Mono.just(locationForecast));
        when(weatherAnalysisService.summarizeToReport(anyList())).thenReturn(forecastReport);
        when(accidentAnalysisService.getRoadAccidentsAssessment(anyList())).thenReturn(accidentReport);

        when(correlateAccidentsAndWeatherService.correlateWeatherAndAccidents(any(WeatherReport.class), any(AccidentReport.class)))
                .thenReturn(correlatedAccidents);

        StepVerifier.create(weatherAndAccidentService.getForecast(from, to, "Test Tag 1"))
                .expectNextMatches(result
                        -> result.route().size() == 2
                && result.forecast() != null
                )
                .verifyComplete();

        verify(routeResolutionService).getSavedRoute(from, to, "Test Tag 1");
        verify(routingService, never()).getRoute(any(Coordinate.class), any(Coordinate.class));

    }

    @Test
    void shouldRequestRouteFromRoutingApiWhenNoSavedRouteFound() {

        when(geoConverterService.getCoordinate(any(Address.class)))
                .thenReturn(Mono.just(fromCoord))
                .thenReturn(Mono.just(toCoord));

        when(routeResolutionService.getSavedRoute(any(Address.class), any(Address.class), any(String.class)))
                .thenReturn(Mono.empty());

        when(routingService.getRoute(any(Coordinate.class), any(Coordinate.class)))
                .thenReturn(Mono.just(coordinates));

        when(routeRequestRepository.save(any())).thenReturn(Mono.just(buildTestSavedRouteEntity()));

        when(mapper.toDomain(any(RouteEntity.class))).thenReturn(savedRoute);
        when(weatherService.getForecast(any(Coordinate.class)))
                .thenReturn(Mono.just(locationForecast));
        when(weatherAnalysisService.summarizeToReport(anyList())).thenReturn(forecastReport);
        when(accidentAnalysisService.getRoadAccidentsAssessment(anyList())).thenReturn(accidentReport);

        when(correlateAccidentsAndWeatherService.correlateWeatherAndAccidents(any(WeatherReport.class), any(AccidentReport.class)))
                .thenReturn(correlatedAccidents);

        StepVerifier.create(weatherAndAccidentService.getForecast(from, to, "NonExistingTag"))
                .expectNextMatches(result
                        -> result.route().size() == 2
                && result.forecast() != null
                )
                .verifyComplete();

        verify(routingService).getRoute(fromCoord, toCoord);
        verify(weatherService, times(2)).getForecast(any(Coordinate.class));
        verify(weatherAnalysisService).summarizeToReport(anyList());
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

    private WeatherReport buildTestForecastReport() {
        return new WeatherReport(
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

    private RouteEntity buildTestSavedRouteEntity() {
        RouteEntity entity = new RouteEntity();
        entity.setId(1L);
        entity.setTag("Test Tag 1");
        entity.setStartStreet(from.getStreet());
        entity.setStartHouseNumber(from.getNum());
        entity.setStartPlz(from.getPlz());
        entity.setStartCity(from.getCity());
        entity.setStartCountry(from.getCountry());
        entity.setEndStreet(to.getStreet());
        entity.setEndHouseNumber(to.getNum());
        entity.setEndPlz(to.getPlz());
        entity.setEndCity(to.getCity());
        entity.setEndCountry(to.getCountry());
        entity.setCoordinates("[[10.0, 53.55], [10.1, 53.56]]");
        return entity;
    }
}
