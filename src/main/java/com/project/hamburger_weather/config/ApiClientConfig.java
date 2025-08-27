package com.project.hamburger_weather.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ApiClientConfig {

    @Value("${apis.weather.base-url}")
    private String weatherBaseUrl;

    @Value("${apis.route.base-url}")
    private String routeBaseUrl;

    @Value("${apis.geoconverter.base-url}")
    private String geoBaseUrl;

    @Value("${apis.geoconverter.api-key}")
    private String geoApiKey;

    @Bean
    public WebClient weatherClient(WebClient.Builder builder) {
        return builder
                .baseUrl(weatherBaseUrl)
                .build();
    }

    @Bean
    public WebClient routeClient(WebClient.Builder builder) {
        return builder
                .baseUrl(routeBaseUrl)
                .build();
    }

    @Bean
    public WebClient geoClient(WebClient.Builder builder) {
        return builder
                .baseUrl(geoBaseUrl)
                .defaultHeader("Authorization", "Bearer " + geoApiKey)
                .build();
    }
}
