package com.project.hamburger_weather.infra.api.mapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.project.hamburger_weather.domain.model.Coordinate;
import com.project.hamburger_weather.domain.model.HourlyForecast;
import com.project.hamburger_weather.domain.model.LocationForecast;
import com.project.hamburger_weather.presentation.dto.HourlyDto;
import com.project.hamburger_weather.presentation.dto.WeatherServiceDto;

@Component
public class WeatherServiceMapper {

    public LocationForecast toLocationForecast(WeatherServiceDto dto) {

        Coordinate coordinate = new Coordinate(dto.longitude(), dto.latitude());

        List<HourlyForecast> hourlyForecasts = toHourlyForecast(dto.hourly());

        return new LocationForecast(coordinate, hourlyForecasts);

    }

    private List<HourlyForecast> toHourlyForecast(HourlyDto hourly) {

        List<HourlyForecast> results = new ArrayList<>();

        for (int i = 0; i < hourly.time().size(); i++) {
            results.add(new HourlyForecast(
                    LocalDateTime.parse(hourly.time().get(i)),
                    hourly.temperature2m().get(i),
                    hourly.precipitationProbability().get(i),
                    hourly.precipitation().get(i),
                    hourly.windSpeed().get(i),
                    hourly.weatherCode().get(i)
            ));
        }

        return results;
    }
}
