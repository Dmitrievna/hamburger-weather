package com.project.hamburger_weather.infra.api;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.project.hamburger_weather.domain.model.Coordinate;
import com.project.hamburger_weather.domain.model.Route;
import com.project.hamburger_weather.infra.api.mapper.RoutingServiceMapper;
import com.project.hamburger_weather.presentation.dto.RoutingServiceDto;

import reactor.core.publisher.Mono;

@Service
public class RoutingService {

    private final WebClient routeClient;
    private final RoutingServiceMapper mapper;

    public RoutingService(@Qualifier("routeClient") WebClient routeClient, RoutingServiceMapper mapper) {
        this.routeClient = routeClient;
        this.mapper = mapper;
    }

    public Mono<Route> getRoute(Coordinate from, Coordinate to) {
        return routeClient.get()
                .uri(uriBuilder -> uriBuilder
                .path("/driving/{startLon},{startLat};{endLon},{endLat}")
                .queryParam("overview", "full")
                .queryParam("geometries", "geojson")
                .build(from.longitude(), from.latitude(), to.longitude(), to.latitude()))
                .retrieve()
                .bodyToMono(RoutingServiceDto.class)
                .map(mapper::toRoute);
    }

}
