package com.project.hamburger_weather.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.hamburger_weather.dto.WmoCodeDto;

import jakarta.annotation.PostConstruct;



@Service
public class WmoCodesService {

    private final ObjectMapper objectMapper;
    private final ResourceLoader resourceLoader;
    private List<WmoCodeDto> wmoCodes;

    public WmoCodesService(ResourceLoader resourceLoader, ObjectMapper objectMapper) throws IOException {
        this.resourceLoader = resourceLoader;
        this.objectMapper = objectMapper;
        loadWmoCodes();
    }

    @PostConstruct
    private void loadWmoCodes() throws IOException {
        Resource resource = resourceLoader.getResource("classpath:static/wmo-codes.json");
        try (InputStream inputStream = resource.getInputStream()) {
            this.wmoCodes = objectMapper.readValue(inputStream,new TypeReference<List<WmoCodeDto>>(){});
        }

    }


    public List<String> getAllGoodCodes() {
        return List.of("0","1","2","3","45");
    }

    public List<WmoCodeDto> getWmoCodes() {
        return wmoCodes;
    }   

    public String getDescriptionByCode(String code) {
        return wmoCodes.stream()
            .filter(wmo -> wmo.code().equals(code))
            .findFirst()
            .map(WmoCodeDto::description)
            .orElse("Unknown code");
    }
    

}
