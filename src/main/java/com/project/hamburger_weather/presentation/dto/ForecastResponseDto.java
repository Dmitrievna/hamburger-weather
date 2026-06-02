package com.project.hamburger_weather.presentation.dto;

import java.util.List;
import com.project.hamburger_weather.domain.model.Coordinate;
import com.project.hamburger_weather.domain.model.RiskLevel;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Response DTO containing route coordinates, weather forecast, accident risk assessment and related information for the next 3 hours based on the calculated route"
)
public record ForecastResponseDto(
        @Schema(description = "Route coordinates including start and finish")
        List<Coordinate> route,
        @Schema(description = "Average temperature in celsius")
        double avgTemperature,
        @Schema(description = "Minimum temperature in celsius")
        double minTemperature,
        @Schema(description = "Maximum temperature in celsius")
        double maxTemperature,
        @Schema(description = "Average precipitation probability")
        double avgPrecipitationProbability,
        @Schema(description = "Indicates if it is going to rain during the route")
        boolean rainy,
        @Schema(description = "Indicates if the weather is good for cycling")
        boolean goodWeather,
        @Schema(description = "Indicates if it is windy during the route")
        boolean windy,
        @Schema(description = "Number of accidents on the route")
        int numberOfAccidents,
        @Schema(description = "Number of accidents correlated with current weather conditions")
        int numberOfRelatedAccidents,
        @Schema(description = "Risk level for accidents on the route")
        RiskLevel accidentRiskLevel
        ) {

}
