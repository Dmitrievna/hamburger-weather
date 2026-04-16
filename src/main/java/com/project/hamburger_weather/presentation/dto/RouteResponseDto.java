package com.project.hamburger_weather.presentation.dto;

import com.project.hamburger_weather.domain.model.Address;
import com.project.hamburger_weather.domain.model.Route;

public record RouteResponseDto(
        String tag,
        Address startAddress,
        Address endAddress,
        Route route,
        String requestedAt
        ) {

}
