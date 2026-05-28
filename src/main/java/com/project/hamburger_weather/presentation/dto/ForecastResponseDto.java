package com.project.hamburger_weather.presentation.dto;

import java.util.List;
import com.project.hamburger_weather.domain.model.Coordinate;
import com.project.hamburger_weather.domain.model.RiskLevel;

public record ForecastResponseDto(
        List<Coordinate> route,
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
