package com.project.hamburger_weather.presentation.dto;

import com.project.hamburger_weather.domain.model.Route;

public record ForecastResponseDto(
        Route route,
        double avgTemperature,
        double minTemperature,
        double maxTemperature,
        double avgPrecipitationProbability,
        boolean rainy,
        boolean goodWeather,
        boolean windy
        ) {

}
