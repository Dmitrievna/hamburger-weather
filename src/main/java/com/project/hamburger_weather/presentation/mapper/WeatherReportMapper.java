package com.project.hamburger_weather.presentation.mapper;

import org.springframework.stereotype.Component;
import com.project.hamburger_weather.domain.model.ForecastReport;

import com.project.hamburger_weather.domain.model.Route;
import com.project.hamburger_weather.presentation.dto.ForecastResponseDto;

@Component
public class WeatherReportMapper {

    public ForecastResponseDto toForecastResponseDto(ForecastReport forecastReport, Route route) {
        return new ForecastResponseDto(
                route,
                forecastReport.avgTemperature(),
                forecastReport.minTemperature(),
                forecastReport.maxTemperature(),
                forecastReport.avgPrecipitationProbability(),
                forecastReport.rainy(),
                forecastReport.goodWeather(),
                forecastReport.windy()
        );
    }
}
