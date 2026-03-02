package com.project.hamburger_weather.presentation;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.hamburger_weather.application.WeatherAnalyzingService;
import com.project.hamburger_weather.domain.model.Address;
import com.project.hamburger_weather.domain.model.Coordinate;
import com.project.hamburger_weather.domain.model.LocationForecast;
import com.project.hamburger_weather.domain.model.Route;
import com.project.hamburger_weather.infra.api.GeoConverterService;
import com.project.hamburger_weather.infra.api.RoutingService;
import com.project.hamburger_weather.presentation.dto.ForecastRequestDto;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/weather")
public class WeatherController {

    private final WeatherAnalyzingService weatherAnalyzingService;
    private final GeoConverterService geoConverterService;
    private final RoutingService routingService;

    public WeatherController(WeatherAnalyzingService weatherAnalyzingService, GeoConverterService geoConverterService, RoutingService routingService) {
        this.weatherAnalyzingService = weatherAnalyzingService;
        this.geoConverterService = geoConverterService;
        this.routingService = routingService;
    }

    @PostMapping("/requestForecast")
    public Mono<List<LocationForecast>> test(@RequestBody ForecastRequestDto request) {
        Address from = new Address(request.start().street(), request.start().num(), request.start().plz(),
                request.start().city(), request.start().country());
        Address to = new Address(request.end().street(), request.end().num(), request.end().plz(),
                request.end().city(), request.end().country());

        Mono<List<LocationForecast>> result = weatherAnalyzingService.analyzeWeatherForTheRoute(from, to)
                .doOnNext(obj -> System.out.println(obj));
        return result;
    }

    @PostMapping("/testCoordinates")
    public Mono<Coordinate> testCoordinates(@RequestBody Address address) {
        Mono<Coordinate> res = geoConverterService.getCoordinate(address);
        return res;
    }

    @GetMapping("/testRoute")
    public Mono<Route> testCoordinates() {
        Coordinate from = new Coordinate(13.388860, 52.517037);
        Coordinate to = new Coordinate(13.428555, 52.523219);
        Mono<Route> res = routingService.getRoute(from, to);
        return res;
    }

}
