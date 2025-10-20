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


    public boolean checkIfGood(String code) {
        List<String> goodOnes = List.of("0","1","2","3","45");
        return goodOnes.contains(code);
    }

    public boolean checkIfOK(String code) {
        List<String> goodOnes = List.of("48","51","61","71");
        return goodOnes.contains(code);
    }

    public boolean checkIfBad(String code) {
        return !(checkIfGood(code) || checkIfOK(code));
    }

    public boolean checkIfRain(String code) {
        List<String> rainOnes = List.of("51","53","55","56","57","61","63","65","66","67","80","81","82");
        return rainOnes.contains(code);
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
