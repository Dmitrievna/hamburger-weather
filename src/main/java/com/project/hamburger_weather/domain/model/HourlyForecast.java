package com.project.hamburger_weather.domain.model;

import java.time.LocalDateTime;

public record HourlyForecast(LocalDateTime time, double temperature, double precipitationProbability, double precipitation, double windSpeed, String weatherCode) {

}
