package com.project.hamburger_weather.presentation.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record WeatherServiceDto(
        double latitude,
        double longitude,
        @JsonProperty("hourly_units")
        HourlyUnitsDto hourlyUnits,
        HourlyDto hourly
        ) {

}
