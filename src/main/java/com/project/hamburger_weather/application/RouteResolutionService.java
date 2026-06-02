package com.project.hamburger_weather.application;

import org.springframework.stereotype.Service;
import com.project.hamburger_weather.domain.model.Address;

import com.project.hamburger_weather.domain.model.SavedRoute;
import com.project.hamburger_weather.infra.persistence.repository.RouteRequestRepository;
import com.project.hamburger_weather.infra.persistence.mapper.SavedRouteMapper;
import reactor.core.publisher.Mono;

// service only responsible for any operations regarding route retrival from a db
@Service
public class RouteResolutionService {

    private final RouteRequestRepository routeRequestRepository;

    private final SavedRouteMapper mapper;

    public RouteResolutionService(RouteRequestRepository routeRequestRepository, SavedRouteMapper mapper) {
        this.routeRequestRepository = routeRequestRepository;

        this.mapper = mapper;
    }

    public Mono<SavedRoute> getRouteByAddress(Address from, Address to) {
        return routeRequestRepository.findByAddresses(
                from.getStreet(), from.getNum(), from.getPlz(), from.getCity(), from.getCountry(),
                to.getStreet(), to.getNum(), to.getPlz(), to.getCity(), to.getCountry())
                .map(mapper::toDomain);
    }

    public Mono<SavedRoute> getSavedRoute(Address from, Address to, String tag) {
        if (tag != null && !tag.isBlank()) {
            return routeRequestRepository.findByTag(tag)
                    .map(mapper::toDomain)
                    .switchIfEmpty(Mono.defer(() -> getRouteByAddress(from, to)));
        }
        return getRouteByAddress(from, to);
    }

    // add change route's tag if all 
}
