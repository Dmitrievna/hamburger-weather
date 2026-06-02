package com.project.hamburger_weather.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.project.hamburger_weather.infra.persistence.entity.RouteEntity;
import com.project.hamburger_weather.infra.persistence.mapper.SavedRouteMapper;
import com.project.hamburger_weather.infra.persistence.repository.RouteRequestRepository;
import reactor.core.publisher.Mono;
import com.project.hamburger_weather.domain.model.Coordinate;
import com.project.hamburger_weather.domain.model.SavedRoute;
import com.project.hamburger_weather.domain.model.Address;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import reactor.test.StepVerifier;
import static org.mockito.Mockito.never;
import com.google.gson.Gson;

@ExtendWith(MockitoExtension.class)
public class RouteResolutionServiceTest {

    @Mock
    private RouteRequestRepository repository;

    @Mock
    private SavedRouteMapper mapper;

    @InjectMocks
    private RouteResolutionService routeResolutionService;

    // test data
    private final Address from = new Address("Musterstraße", "1", "20095", "Hamburg", "Germany");
    private final Address to = new Address("Reeperbahn", "1", "20359", "Hamburg", "Germany");
    private final Coordinate fromCoord = new Coordinate(53.55, 10.0);
    private final Coordinate toCoord = new Coordinate(53.56, 10.1);
    private final List<Coordinate> coordinates = List.of(fromCoord, toCoord);
    private final SavedRoute savedRoute = new SavedRoute("Test Tag 1", from, to, coordinates, null);
    private final RouteEntity routeEntity = buildRouteEntity();

    @Test
    void shouldReturnSavedRouteByTagWhenTagExists() {

        String tag = "Test Tag 1";

        when(repository.findByTag(
                any()))
                .thenReturn(Mono.just(routeEntity));

        when(mapper.toDomain(any(RouteEntity.class)))
                .thenReturn(savedRoute);

        StepVerifier.create(routeResolutionService.getSavedRoute(from, to, tag))
                .expectNext(savedRoute)
                .verifyComplete();

        verify(repository).findByTag(tag);
        verify(repository, never()).findByAddresses(
                from.getStreet(), from.getNum(), from.getPlz(), from.getCity(), from.getCountry(),
                to.getStreet(), to.getNum(), to.getPlz(), to.getCity(), to.getCountry());

    }

    @Test
    void shouldFetchRouteByAddressWhenTheTagDoesntExist() {
        String tag = "Non-existent Tag";

        when(repository.findByTag(tag))
                .thenReturn(Mono.empty());

        when(repository.findByAddresses(
                any(), any(), any(), any(), any(),
                any(), any(), any(), any(), any()))
                .thenReturn(Mono.just(routeEntity));

        when(mapper.toDomain(any(RouteEntity.class)))
                .thenReturn(savedRoute);

        StepVerifier.create(routeResolutionService.getSavedRoute(from, to, tag))
                .expectNext(savedRoute)
                .verifyComplete();

        verify(repository).findByTag(tag);
        verify(repository).findByAddresses(
                from.getStreet(), from.getNum(), from.getPlz(), from.getCity(), from.getCountry(),
                to.getStreet(), to.getNum(), to.getPlz(), to.getCity(), to.getCountry());
    }

    @Test
    void shouldReturnNullWhenTagDoesntExistAndNoRouteForAddress() {
        String tag = "Non-existent Tag";

        when(repository.findByTag(tag))
                .thenReturn(Mono.empty());

        when(repository.findByAddresses(
                any(), any(), any(), any(), any(),
                any(), any(), any(), any(), any()))
                .thenReturn(Mono.empty());

        StepVerifier.create(routeResolutionService.getSavedRoute(from, to, tag))
                .expectNextCount(0)
                .verifyComplete();

        verify(repository).findByTag(tag);
        verify(repository).findByAddresses(
                from.getStreet(), from.getNum(), from.getPlz(), from.getCity(), from.getCountry(),
                to.getStreet(), to.getNum(), to.getPlz(), to.getCity(), to.getCountry());
    }

    private RouteEntity buildRouteEntity() {
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
        entity.setCoordinates(new Gson().toJson(List.of(fromCoord, toCoord)));
        return entity;
    }
}
