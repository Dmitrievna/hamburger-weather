package com.project.hamburger_weather.presentation.dto;

import com.project.hamburger_weather.domain.model.Address;
import com.project.hamburger_weather.domain.model.Coordinate;
import java.util.List;

public record RouteResponseDto(
        String tag,
        Address startAddress,
        Address endAddress,
        List<Coordinate> route,
        String requestedAt
        ) {

}
