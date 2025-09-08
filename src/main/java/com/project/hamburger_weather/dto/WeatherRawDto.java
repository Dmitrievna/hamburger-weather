package com.project.hamburger_weather.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

public record WeatherRawDto(
    String latitude,
    String longitude,
    @JsonProperty("hourly") JsonNode forecastSummary) {};
