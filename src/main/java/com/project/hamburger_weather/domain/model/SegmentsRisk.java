package com.project.hamburger_weather.domain.model;

// risk assessment for each segment of a route
public record SegmentsRisk(
        Coordinate coordinates,
        int totalAccidentsInSegment,
        int accidentsClear,
        int accidentsTwilight,
        int accidentsNight,
        int accidentsDry,
        int accidentsRain,
        int accidentsSnow,
        RiskLevel riskLevel
        ) {

}
