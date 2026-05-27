package com.project.hamburger_weather.presentation.dto;

import com.project.hamburger_weather.domain.model.Route;
import com.project.hamburger_weather.domain.model.RiskLevel;

public record ForecastResponseDto(
        Route route,
        double avgTemperature,
        double minTemperature,
        double maxTemperature,
        double avgPrecipitationProbability,
        boolean rainy,
        boolean goodWeather,
        boolean windy,
        int numberOfAccidents,
        int numberOfRelatedAccidents,
        RiskLevel accidentRiskLevel
        ) {

}
