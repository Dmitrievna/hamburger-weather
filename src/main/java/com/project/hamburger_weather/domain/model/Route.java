package com.project.hamburger_weather.domain.model;

import java.util.List;

public record Route(List<Coordinate> coordinates) {

    public Route {
        coordinates = List.copyOf(coordinates);
    }
}
