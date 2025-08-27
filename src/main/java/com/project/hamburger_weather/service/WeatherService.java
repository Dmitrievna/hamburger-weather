package com.project.hamburger_weather.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Service
public class WeatherService {
    
    private final WebClient weatherClient;

    public WeatherService(@Qualifier("weatherClient") WebClient weatherClient) {
        this.weatherClient = weatherClient;
    }

    public Mono<String> getForecast() {
        return weatherClient.get()
        .uri(uriBuilder -> uriBuilder
                        .path("/forecast")
                        .queryParam("latitude", "53.5814576")
                        .queryParam("longitude", "10.0616873")
                        .queryParam("hourly", "temperature_2m,precipitation_probability,precipitation")
                        .queryParam("timezone", "Europe/Berlin")
                        .queryParam("forecast_days", "1")
                        .queryParam("forecast_hours", "3")
                        .build())
                .retrieve()
                .bodyToMono(String.class);
    }
}
