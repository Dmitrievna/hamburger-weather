package com.project.hamburger_weather.domain.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.project.hamburger_weather.domain.model.WeatherCodes;

import jakarta.annotation.PostConstruct;

@Service
public class WeatherCodesEvaluationService {

    private final ObjectMapper objectMapper;
    private final ResourceLoader resourceLoader;
    private List<WeatherCodes> weatherCodes;

    public WeatherCodesEvaluationService(ResourceLoader resourceLoader, ObjectMapper objectMapper) throws IOException {
        this.resourceLoader = resourceLoader;
        this.objectMapper = objectMapper;
        loadWeatherCodes();
    }

    @PostConstruct
    private void loadWeatherCodes() throws IOException {
        Resource resource = resourceLoader.getResource("classpath:static/wmo-codes.json");
        try (InputStream inputStream = resource.getInputStream()) {
            this.weatherCodes = objectMapper.readValue(inputStream, new TypeReference<List<WeatherCodes>>() {
            });
        }

    }

    public boolean checkIfGood(String code) {
        List<String> goodOnes = List.of("0", "1", "2", "3", "45");
        return goodOnes.contains(code);
    }

    public boolean checkIfOK(String code) {
        List<String> goodOnes = List.of("48", "51", "61", "71");
        return goodOnes.contains(code);
    }

    public boolean checkIfBad(String code) {
        return !(checkIfGood(code) || checkIfOK(code));
    }

    public boolean checkIfRain(String code) {
        List<String> rainOnes = List.of("51", "53", "55", "56", "57", "61", "63", "65", "66", "67", "80", "81", "82");
        return rainOnes.contains(code);
    }

    public boolean checkIfSnow(String code) {
        List<String> snowOnes = List.of("71", "73", "75", "77", "85", "86");
        return snowOnes.contains(code);
    }

    public List<WeatherCodes> getWeatherCodes() {
        return weatherCodes;
    }

    public String getDescriptionByCode(String code) {
        return weatherCodes.stream()
                .filter(wc -> wc.code().equals(code))
                .findFirst()
                .map(WeatherCodes::description)
                .orElse("Unknown code");
    }

}
