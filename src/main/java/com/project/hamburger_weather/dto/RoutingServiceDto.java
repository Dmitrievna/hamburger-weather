package com.project.hamburger_weather.dto;

import java.util.List;
import com.project.hamburger_weather.domain.model.Coordinate;

public record RoutingServiceDto(List<Coordinate> coordinates) {

}
