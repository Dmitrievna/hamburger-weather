package com.project.hamburger_weather;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.hamburger_weather.dto.HourlyForecastDto;
import com.project.hamburger_weather.mapper.RawToWeatherDtoMapper;


@ExtendWith(MockitoExtension.class)
public class RawToWeatherDtoMapperTest {

    @InjectMocks
    private RawToWeatherDtoMapper mapper;




    @Test
    void shouldMapTimeCorrectly() {
        ObjectMapper objectMapper = new ObjectMapper();
        List<HourlyForecastDto> res;

        try {

            JsonNode jsonInput = objectMapper.readTree("{\"time\":[\"2025-12-01T11:00\",\"2025-12-01T12:00\",\"2025-12-01T13:00\"],\"temperature_2m\":[4.6,5.7,6.2],\"precipitation_probability\":[0,0,0],\"precipitation\":[0.0,0.0,0.0],\"wind_speed_10m\":[15.1,15.7,16.4],\"weather_code\":[2,2,2]}");
            res = RawToWeatherDtoMapper.convertJsonNoteToListOfWeatherForecastDto(jsonInput);
        } catch (JsonProcessingException e) {
            System.out.println("JSON processing error: " + e.getMessage());
            return;
        }

    

    }
    
}
