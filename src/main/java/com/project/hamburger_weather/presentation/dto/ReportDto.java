package com.project.hamburger_weather.presentation.dto;

public record ReportDto(
        double avgTemperature,
        double minTemperature,
        double maxTemperature,
        double avgPrecipitationProbability,
        boolean rainy,
        boolean goodWeather,
        boolean windy
        ) {

}
