package com.project.hamburger_weather.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.project.hamburger_weather.dto.WeatherRawDto;

import reactor.core.publisher.Mono;

@Service
public class WeatherService {
    
    private final WebClient weatherClient;

    public WeatherService(@Qualifier("weatherClient") WebClient weatherClient) {
        this.weatherClient = weatherClient;
    }

    public Mono<WeatherRawDto> getForecast(String lon, String lat) {
        return weatherClient.get()
        .uri(uriBuilder -> uriBuilder
                        .path("/forecast")
                        .queryParam("latitude", lon)
                        .queryParam("longitude", lat)
                        .queryParam("hourly", "temperature_2m,precipitation_probability,precipitation")
                        .queryParam("timezone", "Europe/Berlin")
                        .queryParam("forecast_days", "1")
                        .queryParam("forecast_hours", "3")
                        .build())
                .retrieve()
                .bodyToMono(WeatherRawDto.class);
    }
}
