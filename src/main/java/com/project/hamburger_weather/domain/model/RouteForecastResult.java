package com.project.hamburger_weather.domain.model;

import java.util.List;

public record RouteForecastResult(
        List<Coordinate> route,
        ForecastReport forecast,
        int correlatedAccidents,
        int overallAccidents,
        RiskLevel riskLevel
        ) {

}
