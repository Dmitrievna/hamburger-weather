package com.project.hamburger_weather.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AddressDto(
        @NotBlank(message = "Street is required")
        @Schema(description = "Street name", example = "Musterstraße")
        @Size(max = 100, message = "Street must not exceed 100 characters")
        String street,
        @NotBlank(message = "House number is required")
        @Schema(description = "House number", example = "1")
        String num,
        @NotBlank(message = "City is required")
        @Schema(description = "City name", example = "Hamburg")
        String city,
        @NotBlank(message = "Postal code is required")
        @Schema(description = "Postal code", example = "20095")
        @Pattern(regexp = "\\d{5}", message = "Postal code must be 5 digits")
        String plz,
        @NotBlank(message = "Country is required")
        @Schema(description = "Country name", example = "Germany")
        String country
        ) {

}
