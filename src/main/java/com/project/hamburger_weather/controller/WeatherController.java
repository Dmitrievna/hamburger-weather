package com.project.hamburger_weather.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.hamburger_weather.dto.WeatherServiceDto;
import com.project.hamburger_weather.infra.api.GeoConverterService;
import com.project.hamburger_weather.infra.api.RoutingService;
import com.project.hamburger_weather.infra.api.WeatherService;
import com.project.hamburger_weather.domain.model.LocationForecast;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/weather")
public class WeatherController {

    private final WeatherService weatherService;
    private final RoutingService routingService;
    private final GeoConverterService geoConverterService;

    ;

    public WeatherController(WeatherService weatherService, RoutingService routingService, GeoConverterService geoConverterService) {
        this.weatherService = weatherService;
        this.routingService = routingService;
        this.geoConverterService = geoConverterService;

    }

    @GetMapping("/weather")
    public Mono<LocationForecast> test() {

        return weatherService.getForecast("53.5814576", "10.0616873")
                .doOnNext(obj -> System.out.println(obj));
    }

    // @GetMapping("/route")
    // public void route() {
    //     Mono<RoutingSe
    // rviceDto> res = routingService.getRoute("13.378668","52.516481","13.428554","52.523239");
    //     res.map(r -> {
    //         r.coordinates().forEach(c -> System.out.println("Lon: " + c.lon() + ", Lat: " + c.lat()));
    //         return r;
    //     }).subscribe();
    // }
    // @GetMapping("/geo")
    // public void geo() {
    //     Mono<CoordinatesDto> res = geoConverterService.getCoordinates("Invalidenstraße", "116", "10115", "Berlin", "Germany");
    //     res.subscribe(dto -> 
    // System.out.println("Coords: lat=" + dto.lat() + ", lon=" + dto.lon()));
    // }
    // @GetMapping("/report")
    // public void alles() {
    //     Mono<ReportDto> forecast = aggregationService.getTheAnswer(
    //         new AddressDto("Bramfelder Str", "18", "22305", "Hamburg", "Germany"),
    //         new AddressDto("Invalidenstraße", "116", "10115", "Berlin", "Germany")
    //     ).cast(ReportDto.class);        
    //     // forecast.subscribe(dto -> {
    //     //     System.out.println("Avg Temp: " + dto.avgTemperature());
    //     //     System.out.println("Min Temp: " + dto.minTemperature());
    //     //     System.out.println("Max Temp: " + dto.maxTemperature());
    //     //     System.out.println("Avg Precip Prob: " + dto.avgPrecipitationProbability());
    //     //     System.out.println("Rainy: " + dto.rainy());
    //     //     System.out.println("Good Weather: " + dto.goodWeather());
    //     //     System.out.println("Windy: " + dto.windy());
    //     // });
    //     forecast.subscribe(x ->{
    //     System.out.println(x);
    //  });
    // }
    // @PostMapping("/forecast")
    //     public Mono<ReportDto> getForecastReport(
    //         @RequestBody RouteRequestDto req
    //     ) {
    //         return aggregationService.getTheAnswer(req.start(), req.end());
    // }
}
