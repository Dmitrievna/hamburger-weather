package com.project.hamburger_weather.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.hamburger_weather.dto.CoordinatesDto;
import com.project.hamburger_weather.dto.RouteDto;
import com.project.hamburger_weather.dto.WeatherForecastDto;
import com.project.hamburger_weather.dto.WeatherRawDto;
import com.project.hamburger_weather.mapper.RawToWeatherDtoMapper;
import com.project.hamburger_weather.service.GeoConverterService;
import com.project.hamburger_weather.service.RoutingService;
import com.project.hamburger_weather.service.WeatherService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/weather")
public class WeatherController {

    private final WeatherService weatherService;
    private final RoutingService routingService;
    private final GeoConverterService geoConverterService;

    public WeatherController(WeatherService weatherService, RoutingService routingService, GeoConverterService geoConverterService) {
        this.weatherService = weatherService;
        this.routingService = routingService;
        this.geoConverterService = geoConverterService;
    }

    @GetMapping("/forecast")
    public String forecast() {
        return "Hi weather!";
    }

    @GetMapping("/weather")
    public void test() {
        Mono<WeatherRawDto> res = weatherService.getForecast("53.5814576", "10.0616873");

        Mono<WeatherForecastDto> mapped = res.map(raw -> {
            return RawToWeatherDtoMapper.convert(raw);
        });

        mapped.map(dto -> {
            System.out.println("Coordinates: lat=" + dto.coordinates().lat() + ", lon=" + dto.coordinates().lon());
            dto.hourlyForecast().forEach(hour -> {
                System.out.println(hour.getAll());
            });
            return dto;
        }).subscribe();
    
    }

    @GetMapping("/route")
    public void route() {
        Mono<RouteDto> res = routingService.getRoute("13.378668","52.516481","13.428554","52.523239");
        res.map(r -> {
            r.coordinates().forEach(c -> System.out.println("Lon: " + c.lon() + ", Lat: " + c.lat()));
            return r;
        }).subscribe();
    }

    @GetMapping("/geo")
    public void geo() {
        Mono<CoordinatesDto> res = geoConverterService.getCoordinates("Invalidenstraße", "116", "10115", "Berlin", "Germany");
        res.subscribe(dto -> 
    System.out.println("Coords: lat=" + dto.lat() + ", lon=" + dto.lon()));
    }
}
