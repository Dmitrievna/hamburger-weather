package com.project.hamburger_weather.domain.model;

import java.time.LocalDateTime;

public record SavedRoute(
        String tag,
        Address startAddress,
        Address endAddress,
        Route route,
        LocalDateTime requestedAt
        ) {

}
