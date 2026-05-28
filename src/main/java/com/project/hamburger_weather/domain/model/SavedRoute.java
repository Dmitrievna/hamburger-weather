package com.project.hamburger_weather.domain.model;

import java.util.List;

import java.time.LocalDateTime;

public record SavedRoute(
        String tag,
        Address startAddress,
        Address endAddress,
        List<Coordinate> coordinates,
        LocalDateTime requestedAt
        ) {

}
