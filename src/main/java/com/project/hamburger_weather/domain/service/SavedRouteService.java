package com.project.hamburger_weather.domain.service;

import com.project.hamburger_weather.domain.model.Route;
import com.project.hamburger_weather.persistence.entity.RouteEntity;
import com.project.hamburger_weather.persistence.repository.RouteRequestRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import com.project.hamburger_weather.domain.model.SavedRoute;
import com.project.hamburger_weather.persistence.mapper.SavedRouteMapper;
import com.project.hamburger_weather.exception.RouteNotFoundException;

@Service
public class SavedRouteService {

    private final RouteRequestRepository routeRequestRepository;
    private final SavedRouteMapper mapper;

    public SavedRouteService(RouteRequestRepository routeRequestRepository, SavedRouteMapper mapper) {
        this.routeRequestRepository = routeRequestRepository;
        this.mapper = mapper;
    }

    public Mono<SavedRoute> getRouteByTag(String tag) {
        return routeRequestRepository.findByTag(tag)
                .map(mapper::toDomain)
                .switchIfEmpty(Mono.error(new RouteNotFoundException("Route with tag " + tag + " not found")));
    }

}
