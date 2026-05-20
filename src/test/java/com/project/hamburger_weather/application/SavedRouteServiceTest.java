package com.project.hamburger_weather.application;

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
import com.project.hamburger_weather.domain.model.Coordinate;
import com.project.hamburger_weather.domain.model.SavedRoute;
import com.project.hamburger_weather.infra.api.GeoConverterService;
import com.project.hamburger_weather.infra.api.RoutingService;
import com.project.hamburger_weather.domain.model.Address;
import com.project.hamburger_weather.domain.model.Route;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import reactor.test.StepVerifier;
import static org.mockito.Mockito.never;
import com.project.hamburger_weather.exception.TagNotFoundException;

@ExtendWith(MockitoExtension.class)
public class SavedRouteServiceTest {

    @Mock
    private RouteRequestRepository repository;

    @Mock
    private RoutingService routingService;

    @Mock
    private GeoConverterService geoConverterService;

    @Mock
    private CoordinatesOptimizator coordinatesOptimizator;

    @Mock
    private SavedRouteMapper mapper;

    @InjectMocks
    private SavedRouteService savedRouteService;

    // test data
    private final Address from = new Address("Musterstraße", "1", "20095", "Hamburg", "Germany");
    private final Address to = new Address("Reeperbahn", "1", "20359", "Hamburg", "Germany");
    private final Coordinate fromCoord = new Coordinate(53.55, 10.0);
    private final Coordinate toCoord = new Coordinate(53.56, 10.1);
    private final SavedRoute savedRoute = new SavedRoute("Test Tag 1", from, to, new Route(List.of(fromCoord, toCoord)), null);

    @Test
    void shouldReturnSavedRouteWhenRouteExistsInDb() {

        when(repository.existsByAddresses(
                any(), any(), any(), any(), any(),
                any(), any(), any(), any(), any()))
                .thenReturn(Mono.just(true));

        when(repository.findByAddresses(
                any(), any(), any(), any(), any(),
                any(), any(), any(), any(), any()))
                .thenReturn(Mono.just(new RouteEntity()));

        when(mapper.toDomain(any(RouteEntity.class)))
                .thenReturn(savedRoute);

        StepVerifier.create(savedRouteService.getRouteByAddress(from, to))
                .expectNext(savedRoute)
                .verifyComplete();

        // check we never called routing and geo api services
        verify(routingService, never()).getRoute(any(), any());
        verify(geoConverterService, never()).getCoordinate(any());

    }

    @Test
    void shouldFetchRouteFromRoutingServiceAndSaveToDbWhenRouteDoesNotExistInDb() {
        when(repository.existsByAddresses(
                any(), any(), any(), any(), any(),
                any(), any(), any(), any(), any()))
                .thenReturn(Mono.just(false));

        when(geoConverterService.getCoordinate(any()))
                .thenReturn(Mono.just(fromCoord))
                .thenReturn(Mono.just(toCoord));

        when(routingService.getRoute(any(), any()))
                .thenReturn(Mono.just(new Route(List.of(fromCoord, toCoord))));

        when(coordinatesOptimizator.deduplicate(any(Route.class)))
                .thenReturn(new Route(List.of(fromCoord, toCoord)));

        when(mapper.toEntity(any(), any(), any(), any(Route.class)))
                .thenReturn(new RouteEntity());

        when(repository.save(any(RouteEntity.class)))
                .thenReturn(Mono.just(new RouteEntity()));

        when(mapper.toDomain(any(RouteEntity.class)))
                .thenReturn(savedRoute);

        StepVerifier.create(savedRouteService.getRouteByAddress(from, to))
                .expectNext(savedRoute)
                .verifyComplete();

        // check we called routing and geo api services
        verify(geoConverterService).getCoordinate(from);
        verify(geoConverterService).getCoordinate(to);
        verify(routingService).getRoute(fromCoord, toCoord);
    }

    @Test
    void shouldReturnErrorWhenRoutingServiceIsNotResponding() {
        when(repository.existsByAddresses(
                any(), any(), any(), any(), any(),
                any(), any(), any(), any(), any()))
                .thenReturn(Mono.just(false));

        when(geoConverterService.getCoordinate(any()))
                .thenReturn(Mono.just(fromCoord))
                .thenReturn(Mono.just(toCoord));

        when(routingService.getRoute(any(), any()))
                .thenReturn(Mono.error(new RuntimeException("Error while fetching route from routing service")));

        StepVerifier.create(savedRouteService.getRouteByAddress(from, to))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException
                && throwable.getMessage().equals("Error while fetching route from routing service"))
                .verify();

        // check we called routing and geo api services
        verify(geoConverterService).getCoordinate(from);
        verify(geoConverterService).getCoordinate(to);
        verify(routingService).getRoute(fromCoord, toCoord);
    }

    @Test 
    void shouldReturnErrorWhenGeoConverterServiceIsNotResponding() {
        when(repository.existsByAddresses(
                any(), any(), any(), any(), any(),
                any(), any(), any(), any(), any()))
                .thenReturn(Mono.just(false));

        when(geoConverterService.getCoordinate(any()))
                .thenReturn(Mono.error(new RuntimeException("Error while fetching coordinates from geocoding service")));

        StepVerifier.create(savedRouteService.getRouteByAddress(from, to))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException
                && throwable.getMessage().equals("Error while fetching coordinates from geocoding service"))
                .verify();

        // check we called geo api service and never called routing service
        verify(geoConverterService).getCoordinate(from);
        verify(routingService, never()).getRoute(any(), any());
    }

    @Test
    void shouldReturnRouteForGivenTag() {
        when(repository.existsByTag(any()))
                .thenReturn(Mono.just(true));

        when(repository.findByTag(any()))
                .thenReturn(Mono.just(new RouteEntity()));

        when(mapper.toDomain(any(RouteEntity.class)))
                .thenReturn(savedRoute);

        StepVerifier.create(savedRouteService.getRouteByTag("Test Tag 1"))
                .expectNext(savedRoute)
                .verifyComplete();
    }

    @Test
    void shouldReturnErrorWhenRouteNotFoundForGivenTag() {
        when(repository.existsByTag(any()))
                .thenReturn(Mono.just(false));

        StepVerifier.create(savedRouteService.getRouteByTag("NonExistingTag"))
                .expectErrorMatches(throwable -> throwable instanceof TagNotFoundException
                && throwable.getMessage().equals("Route with tag NonExistingTag not found"))
                .verify();
    }
}
