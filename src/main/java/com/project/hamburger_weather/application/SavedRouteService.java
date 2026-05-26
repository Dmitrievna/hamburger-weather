package com.project.hamburger_weather.application;

import org.springframework.stereotype.Service;
import com.project.hamburger_weather.domain.model.Address;
import com.project.hamburger_weather.domain.model.Coordinate;
import com.project.hamburger_weather.domain.model.SavedRoute;
import com.project.hamburger_weather.infra.persistence.repository.RouteRequestRepository;
import com.project.hamburger_weather.infra.persistence.entity.RouteEntity;
import com.project.hamburger_weather.infra.persistence.mapper.SavedRouteMapper;
import reactor.core.publisher.Mono;
import com.project.hamburger_weather.exception.RouteNotFoundException;
import com.project.hamburger_weather.exception.TagNotFoundException;
import com.project.hamburger_weather.infra.api.RoutingService;
import com.project.hamburger_weather.infra.support.CoordinatesOptimizator;
import com.project.hamburger_weather.infra.api.GeoConverterService;
import com.project.hamburger_weather.domain.model.Route;

@Service
public class SavedRouteService {

    private final RouteRequestRepository routeRequestRepository;
    private final RoutingService routingService;
    private final SavedRouteMapper mapper;
    private final CoordinatesOptimizator coordinatesOptimizator;
    private final GeoConverterService geoConverterService;

    public SavedRouteService(RouteRequestRepository routeRequestRepository, RoutingService routingService, SavedRouteMapper mapper, CoordinatesOptimizator coordinatesOptimizator, GeoConverterService geoConverterService) {
        this.routeRequestRepository = routeRequestRepository;
        this.routingService = routingService;
        this.mapper = mapper;
        this.coordinatesOptimizator = coordinatesOptimizator;
        this.geoConverterService = geoConverterService;
    }

    public Mono<SavedRoute> getRouteByAddress(Address from, Address to) {
        return routeRequestRepository.existsByAddresses(from.street(), from.num(), from.plz(), from.city(), from.country(), to.street(), to.num(), to.plz(), to.city(), to.country())
                .flatMap(exists -> {
                    if (exists) {
                        return routeRequestRepository.findByAddresses(from.street(), from.num(), from.plz(), from.city(), from.country(), to.street(), to.num(), to.plz(), to.city(), to.country())
                                .map(mapper::toDomain);
                    } else {
                        Mono<Coordinate> fromCoordinate = geoConverterService.getCoordinate(from);
                        Mono<Coordinate> toCoordinate = geoConverterService.getCoordinate(to);

                        return Mono.zip(fromCoordinate, toCoordinate)
                                .flatMap(tuple -> routingService.getRoute(tuple.getT1(), tuple.getT2()))
                                .flatMap(r -> {
                                    Route deduplicated = coordinatesOptimizator.deduplicate(r);
                                    // todo figure out how to generate a tag for the route, maybe use a hash of the coordinates or a combination of the addresses
                                    RouteEntity entity = mapper.toEntity("Test Tag 1", from, to, deduplicated);
                                    return routeRequestRepository
                                            .save(entity)
                                            .thenReturn(entity)
                                            .map(mapper::toDomain);
                                });

                    }
                }).cache();
        //cache is to avoid double subscribtion

    }

    ;
    public Mono<SavedRoute> getRouteByTag(String tag) {
        return routeRequestRepository.existsByTag(tag)
                .flatMap(exists -> {
                    if (exists) {
                        return routeRequestRepository.findByTag(tag)
                                .map(mapper::toDomain);
                    } else {
                        return Mono.error(new TagNotFoundException("Route with tag " + tag + " not found"));
                    }
                });
    }
}
