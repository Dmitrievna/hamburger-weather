package com.project.hamburger_weather.domain.model;

public record WeatherReport(
        double avgTemperature,
        double minTemperature,
        double maxTemperature,
        double avgPrecipitationProbability,
        boolean rainy,
        boolean goodWeather,
        boolean windy,
        boolean snowy
        ) {

}
