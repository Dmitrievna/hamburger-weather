package com.project.hamburger_weather.persistence.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.hamburger_weather.domain.model.Coordinate;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import java.util.List;

@WritingConverter
public class CoordinateListToStringConverter implements Converter<List<Coordinate>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convert(List<Coordinate> source) {
        try {
            return objectMapper.writeValueAsString(source);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize coordinate list", e);
        }
    }
}
