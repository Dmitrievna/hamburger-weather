package com.project.hamburger_weather.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record HourlyUnitsDto(
        String time,
        @JsonProperty("temperature_2m")
        String temperature2m,
        @JsonProperty("precipitation_probability")
        String precipitationProbability,
        String precipitation,
        @JsonProperty("wind_speed_10m")
        String windSpeed,
        @JsonProperty("weather_code")
        String weatherCode
        ) {

}
