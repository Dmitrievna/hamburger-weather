package com.project.hamburger_weather.persistence.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.hamburger_weather.domain.model.Coordinate;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import java.util.List;

@ReadingConverter
public class StringToCoordinateListConverter implements Converter<String, List<Coordinate>> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public List<Coordinate> convert(String source) {
        try {
            return objectMapper.readValue(source, new TypeReference<List<Coordinate>>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize coordinate list", e);
        }
    }
}
