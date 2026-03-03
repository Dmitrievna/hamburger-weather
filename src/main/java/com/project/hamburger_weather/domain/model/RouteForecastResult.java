package com.project.hamburger_weather.domain.model;

public record RouteForecastResult(
        Route route,
        ForecastReport forecast
        ) {

}
