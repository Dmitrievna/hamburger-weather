package com.project.hamburger_weather.presentation.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record ForecastRequestDto(
        @NotNull(message = "Start address is required")
        @Valid
        AddressDto startAddress,
        @NotNull(message = "End address is required")
        @Valid
        AddressDto endAddress,
        @Pattern(regexp = "^[a-zA-Z0-9-_ ]{1,50}$",
                message = "Tag must be alphanumeric, max 50 characters")
        String tag
        ) {

}
