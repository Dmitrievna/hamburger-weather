package com.project.hamburger_weather.presentation.dto;

import java.util.List;

public record GeometryDto(
        List<List<Double>> coordinates
        ) {

}
