package com.project.hamburger_weather.infra.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.project.hamburger_weather.domain.model.Coordinate;
import com.project.hamburger_weather.dto.RoutingServiceDto;

import reactor.core.publisher.Mono;

@Service
public class RoutingService {

    private final WebClient routeClient;

    public RoutingService(@Qualifier("routeClient") WebClient routeClient) {
        this.routeClient = routeClient;
    }

    public Mono<List<Coordinate>> getRoute(double startLon, double startLat, double endLon, double endLat) {
        return routeClient.get()
                .uri(uriBuilder -> uriBuilder
                .path("/driving/{startLon},{startLat};{endLon},{endLat}")
                .queryParam("overview", "full")
                .queryParam("geometries", "geojson")
                .build(startLon, startLat, endLon, endLat))
                .retrieve()
                .bodyToMono(RoutingServiceDto.class)
                .map(dto -> dto.coordinates());
    }

}
