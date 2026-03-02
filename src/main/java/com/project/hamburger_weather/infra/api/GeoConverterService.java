package com.project.hamburger_weather.infra.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.project.hamburger_weather.domain.model.Address;
import com.project.hamburger_weather.domain.model.Coordinate;
import com.project.hamburger_weather.presentation.dto.GeoConverterServiceDto;

import reactor.core.publisher.Mono;

@Service
public class GeoConverterService {

    private final WebClient geoClient;

    public GeoConverterService(@Qualifier("geoClient") WebClient geoClient) {
        this.geoClient = geoClient;
    }

    public Mono<Coordinate> getCoordinate(Address address) {
        // Implement the logic to call the geocoding API and return coordinates as a String
        return geoClient.get()
                .uri(uriBuilder -> uriBuilder
                .path("/search")
                .queryParam("q", "{street} {num} {PLZ} {city} {country}")
                .build(address.street(), address.num(), address.plz(), address.city(), address.country()))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<GeoConverterServiceDto>>() {
                })
                .map(results -> {
                    if (results.isEmpty()) {
                        throw new RuntimeException("No geocoding result found");
                    }

                    GeoConverterServiceDto first = results.get(0);
                    return new Coordinate(
                            Double.valueOf(first.lon()),
                            Double.valueOf(first.lat())
                    );
                });
    }
}
