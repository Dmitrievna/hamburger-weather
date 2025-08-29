package com.project.hamburger_weather.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.project.hamburger_weather.dto.CoordinatesDto;

import reactor.core.publisher.Mono;

@Service
public class GeoConverterService {

    private final WebClient geoClient;
    
    public GeoConverterService(@Qualifier("geoClient") WebClient geoClient) {
        this.geoClient = geoClient;
    }

    public Mono<CoordinatesDto> getCoordinates(String street, String num, String plz, String city, String country) {
        // Implement the logic to call the geocoding API and return coordinates as a String
       return  geoClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search")
                        .queryParam("q", "{street}+{num}+{PLZ}+{city}+{country}")
                        .build(street, num, plz, city, country))
                .retrieve()
                .bodyToFlux(CoordinatesDto.class)
                .next();
    }
}
