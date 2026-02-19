package com.project.hamburger_weather.dto;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record HourlyDto(
        List<String> time,
        @JsonProperty("temperature_2m")
        List<Double> temperature2m,
        @JsonProperty("precipitation_probability")
        List<Integer> precipitationProbability,
        List<Double> precipitation,
        @JsonProperty("wind_speed_10m")
        List<Double> windSpeed,
        @JsonProperty("weather_code")
        List<String> weatherCode
        ) {

}
