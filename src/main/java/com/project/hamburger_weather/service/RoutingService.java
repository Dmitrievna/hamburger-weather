package com.project.hamburger_weather.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.project.hamburger_weather.dto.RouteDto;

import reactor.core.publisher.Mono;

@Service
public class RoutingService {
	private final WebClient routeClient;

    public RoutingService(@Qualifier("routeClient") WebClient routeClient) {
        this.routeClient = routeClient;
    }

    public Mono<RouteDto> getRoute(String startLon, String startLat, String endLon, String endLat) {
        return routeClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/driving/{startLon},{startLat};{endLon},{endLat}")
                        .queryParam("overview", "full")
                        .queryParam("geometries", "geojson")
                        .build(startLon, startLat, endLon, endLat))
                .retrieve()
                .bodyToMono(RouteDto.class);
    }
}
