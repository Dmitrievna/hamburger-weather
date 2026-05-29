package com.project.hamburger_weather.presentation.mapper;

import org.springframework.stereotype.Component;

import com.project.hamburger_weather.domain.model.ForecastReport;

import com.project.hamburger_weather.domain.model.Route;
import com.project.hamburger_weather.domain.model.RouteForecastResult;
import com.project.hamburger_weather.presentation.dto.ForecastResponseDto;

@Component
public class WeatherReportMapper {

    public ForecastResponseDto toForecastResponseDto(RouteForecastResult routeForecastResult) {
        return new ForecastResponseDto(
                routeForecastResult.route(),
                routeForecastResult.forecast().avgTemperature(),
                routeForecastResult.forecast().minTemperature(),
                routeForecastResult.forecast().maxTemperature(),
                routeForecastResult.forecast().avgPrecipitationProbability(),
                routeForecastResult.forecast().rainy(),
                routeForecastResult.forecast().goodWeather(),
                routeForecastResult.forecast().windy(),
                routeForecastResult.overallAccidents(),
                routeForecastResult.correlatedAccidents(),
                routeForecastResult.riskLevel()
        );
    }
}
