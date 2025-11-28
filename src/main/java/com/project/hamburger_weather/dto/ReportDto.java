package com.project.hamburger_weather.dto;

public record ReportDto(
    double avgTemperature,
    double minTemperature,
    double maxTemperature,
    double avgPrecipitationProbability,
    boolean rainy,
    boolean goodWeather,
    boolean windy
) {}