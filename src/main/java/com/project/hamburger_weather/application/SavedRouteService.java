package com.project.hamburger_weather.application;

import org.springframework.stereotype.Service;
import com.project.hamburger_weather.domain.model.Address;
import com.project.hamburger_weather.domain.model.SavedRoute;
import com.project.hamburger_weather.persistence.repository.RouteRequestRepository;
import com.project.hamburger_weather.persistence.mapper.SavedRouteMapper;
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

    public Mono<SavedRoute> getRouteByAddress(Address start, Address end) {
        return routeRequestRepository.existsByAddresses(start, end)
                .flatMap(exists -> {
                    if (exists) {
                        return routeRequestRepository.findByAddresses(start, end)
                                .map(mapper::toDomain);
                    } else {
                        return Mono.error(new RouteNotFoundException("Route from " + start + " to " + end + " not found"));
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
