package com.project.hamburger_weather.presentation.dto;

import com.project.hamburger_weather.domain.model.Address;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record ForecastRequestDto(
        @NotNull(message = "Start address is required")
        @Valid
        Address start,
        @NotNull(message = "End address is required")
        @Valid
        Address end
        ) {

}
