package com.project.hamburger_weather.presentation.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record ForecastRequestDto(
        @NotNull(message = "Start address is required")
        @Valid
        AddressDto start,
        @NotNull(message = "End address is required")
        @Valid
        AddressDto end
        ) {

}
