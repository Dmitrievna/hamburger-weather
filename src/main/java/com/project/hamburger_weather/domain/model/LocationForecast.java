package com.project.hamburger_weather.domain.model;

import java.util.List;

public record LocationForecast(Coordinate coordinate, List<HourlyForecast> hourlyForecasts) {

}
