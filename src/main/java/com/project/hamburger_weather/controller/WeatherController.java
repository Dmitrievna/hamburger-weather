package com.project.hamburger_weather.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.hamburger_weather.dto.AddressDto;
import com.project.hamburger_weather.dto.CoordinatesDto;
import com.project.hamburger_weather.dto.ReportDto;
import com.project.hamburger_weather.dto.RouteDto;
import com.project.hamburger_weather.dto.WeatherForecastDto;
import com.project.hamburger_weather.dto.WeatherRawDto;
import com.project.hamburger_weather.mapper.RawToWeatherDtoMapper;
import com.project.hamburger_weather.service.AggregationService;
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
    private final AggregationService aggregationService;

    public WeatherController(WeatherService weatherService, RoutingService routingService, GeoConverterService geoConverterService, AggregationService aggregationService) {
        this.weatherService = weatherService;
        this.routingService = routingService;
        this.geoConverterService = geoConverterService;
        this.aggregationService = aggregationService;
    }

    @GetMapping("/forecast")
    public String forecast() {
        return "Hi weather!";
    }

    @GetMapping("/weather")
    public void test() {
        Mono<WeatherRawDto> res = weatherService.getForecast("53.5814576", "10.0616873");

        // res.map(raw -> {
        //     System.out.println("Raw weather data received: " + raw);
        //     return raw;
        // }).subscribe();

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


    @GetMapping("/report")
    public void alles() {
        Mono<ReportDto> forecast = aggregationService.getTheAnswer(
            new AddressDto("Beimoorstr", "18", "22081", "Hamburg", "Germany"),
            new AddressDto("Conventstraße", "8-10", "22089", "Hamburg", "Germany")
        );

        forecast.subscribe(dto -> {
            System.out.println("Avg Temp: " + dto.avgTemperature());
            System.out.println("Min Temp: " + dto.minTemperature());
            System.out.println("Max Temp: " + dto.maxTemperature());
            System.out.println("Avg Precip Prob: " + dto.avgPrecipitationProbability());
            System.out.println("Rainy: " + dto.rainy());
            System.out.println("Good Weather: " + dto.goodWeather());
            System.out.println("Windy: " + dto.windy());
        });
    }
}
