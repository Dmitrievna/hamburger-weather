package com.project.hamburger_weather.infra.api;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.project.hamburger_weather.domain.model.Coordinate;
import com.project.hamburger_weather.domain.model.LocationForecast;
import com.project.hamburger_weather.infra.api.mapper.WeatherServiceMapper;
import com.project.hamburger_weather.presentation.dto.WeatherServiceDto;

import reactor.core.publisher.Mono;

@Service
public class WeatherService {

    private final WebClient weatherClient;
    private final WeatherServiceMapper mapper;

    public WeatherService(@Qualifier("weatherClient") WebClient weatherClient, WeatherServiceMapper mapper) {
        this.weatherClient = weatherClient;
        this.mapper = mapper;
    }

    public Mono<LocationForecast> getForecast(Coordinate coordinate) {
        return weatherClient.get()
                .uri(uriBuilder -> uriBuilder
                .path("/forecast")
                .queryParam("latitude", coordinate.latitude())
                .queryParam("longitude", coordinate.longitude())
                .queryParam("hourly", "temperature_2m,precipitation_probability,precipitation,wind_speed_10m,weather_code")
                .queryParam("timezone", "Europe/Berlin")
                .queryParam("forecast_days", "1")
                .queryParam("forecast_hours", "3")
                .build())
                .retrieve()
                .bodyToMono(WeatherServiceDto.class)
                .map(mapper::toLocationForecast);
    }
}
