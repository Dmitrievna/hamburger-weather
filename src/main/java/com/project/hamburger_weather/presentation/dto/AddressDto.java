package com.project.hamburger_weather.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AddressDto(
        @NotBlank(message = "Street is required")
        @Size(max = 100, message = "Street must not exceed 100 characters")
        String street,
        @NotBlank(message = "House number is required")
        String num,
        @NotBlank(message = "Postal code is required")
        @Pattern(regexp = "\\d{5}", message = "Postal code must be 5 digits")
        String plz,
        @NotBlank(message = "City is required")
        String city,
        @NotBlank(message = "Country is required")
        String country
        ) {

}
