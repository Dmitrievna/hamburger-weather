package com.project.hamburger_weather.domain.model;

import java.util.List;

/// todo mmm rename savedRoute or Route because it is confusing
public record Route(List<Coordinate> coordinates) {

    public Route {
        coordinates = List.copyOf(coordinates);
    }
}
