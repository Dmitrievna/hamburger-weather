package com.project.hamburger_weather.application;

import org.springframework.stereotype.Service;
import com.project.hamburger_weather.domain.model.Address;
import com.project.hamburger_weather.domain.model.SavedRoute;
import com.project.hamburger_weather.infra.persistence.repository.RouteRequestRepository;
import com.project.hamburger_weather.infra.persistence.mapper.SavedRouteMapper;
import reactor.core.publisher.Mono;
import com.project.hamburger_weather.exception.RouteNotFoundException;
import com.project.hamburger_weather.exception.TagNotFoundException;

@Service
public class SavedRouteService {

    private final RouteRequestRepository routeRequestRepository;
    private final SavedRouteMapper mapper;

    public SavedRouteService(RouteRequestRepository routeRequestRepository, SavedRouteMapper mapper) {
        this.routeRequestRepository = routeRequestRepository;
        this.mapper = mapper;
    }

    public Mono<SavedRoute> getRouteByAddress(Address from, Address to) {
        return routeRequestRepository.existsByAddresses(from.street(), from.city(), from.country(), to.street(), to.city(), to.country())
                .flatMap(exists -> {
                    if (exists) {
                        return routeRequestRepository.findByAddresses(from.street(), from.city(), from.country(), to.street(), to.city(), to.country())
                                .map(mapper::toDomain);
                    } else {
                        return Mono.error(new RouteNotFoundException("Route from " + from + " to " + to + " not found"));
                    }
                });

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
