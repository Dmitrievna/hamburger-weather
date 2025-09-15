package com.project.hamburger_weather.mapper;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.project.hamburger_weather.dto.CoordinatesDto;
import com.project.hamburger_weather.dto.HourlyForecastDto;
import com.project.hamburger_weather.dto.WeatherForecastDto;
import com.project.hamburger_weather.dto.WeatherRawDto;
import com.project.hamburger_weather.exception.MappingException;



public class RawToWeatherDtoMapper {
    public static WeatherForecastDto convert(WeatherRawDto raw) {
        return new WeatherForecastDto(
            new CoordinatesDto(raw.latitude(),
            raw.longitude()),
            convertJsonNoteToListOfWeatherForecastDto(raw.forecastSummary())
        );
    }

    public static List<HourlyForecastDto> convertJsonNoteToListOfWeatherForecastDto(JsonNode rawSummary) {
        List<HourlyForecastDto> list = new ArrayList<>(); 
        rawSummary.get("time").forEach(t -> {
            try {
                String time = t.asText();
                String temperature = rawSummary.get("temperature_2m").get(list.size()).asText();
                String precipitationProbability = rawSummary.get("precipitation_probability").get(list.size()).asText();
                String precipitation = rawSummary.get("precipitation").get(list.size()).asText();
                String windSpeed = rawSummary.get("wind_speed_10m").get(list.size()).asText();
                String weatherCode = rawSummary.get("weather_code").get(list.size()).asText();
                list.add(new HourlyForecastDto(time, temperature, precipitationProbability, precipitation, windSpeed, weatherCode));
            } catch (ParseException e) {
                throw new MappingException("Invalid date format in hourly podcast: " + t.asText(), e);
            }
            });
        return list;
    }
}
