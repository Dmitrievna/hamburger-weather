package com.project.hamburger_weather.presentation.dto;

import com.project.hamburger_weather.domain.model.Address;

public record ForecastRequestDto(
        Address start,
        Address end
        ) {

}
