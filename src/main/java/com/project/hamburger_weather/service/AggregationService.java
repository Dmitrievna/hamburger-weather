package com.project.hamburger_weather.service;

import org.springframework.stereotype.Service;

@Service
public class AggregationService {
    private final WeatherService weatherService;
    private final RoutingService routingService;
    private final GeoConverterService geoConverterService;

    public AggregationService(WeatherService weatherService, RoutingService routingService, GeoConverterService geoConverterService) {
        this.weatherService = weatherService;
        this.routingService = routingService;
        this.geoConverterService = geoConverterService;
    }
}
