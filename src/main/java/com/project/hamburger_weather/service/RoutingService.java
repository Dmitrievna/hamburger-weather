package com.project.hamburger_weather.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.hamburger_weather.dto.CoordinatesDto;
import com.project.hamburger_weather.dto.RouteDto;
import com.project.hamburger_weather.exception.RouteParsingException;

import reactor.core.publisher.Mono;

@Service
public class RoutingService {
	private final WebClient routeClient;
    private final ObjectMapper objectMapper;

    public RoutingService(@Qualifier("routeClient") WebClient routeClient, ObjectMapper objectMapper) {
        this.routeClient = routeClient;
        this.objectMapper = objectMapper;
    }

    public Mono<RouteDto> getRoute(String startLon, String startLat, String endLon, String endLat) {
        return routeClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/driving/{startLon},{startLat};{endLon},{endLat}")
                        .queryParam("overview", "full")
                        .queryParam("geometries", "geojson")
                        .build(startLon, startLat, endLon, endLat))
                .retrieve()
                .bodyToMono(String.class)
                .map( json -> {
                    
                    return parseRouteJson(json);
                      
                }
                );
    }

    private RouteDto parseRouteJson(String json) {
        try {
        JsonNode root = objectMapper.readTree(json);
        JsonNode arr = root.path("routes")
                                        .get(0)
                                        .path("geometry")
                                        .path("coordinates"); 
        return convertToRouteDto(arr); 
        } catch (IOException e) {
            throw new RouteParsingException("Failed to parse route JSON", e);
        }
        
                                                
        
        
        
    }

    public RouteDto convertToRouteDto(JsonNode node) {
        List<CoordinatesDto> coordinates = new ArrayList<>();

        for(JsonNode pair: node) {
            String lon = pair.get(0).asText();
            String lat = pair.get(1).asText();
            coordinates.add(new CoordinatesDto(lat, lon));
        }

        return new RouteDto(coordinates);
    }

}
