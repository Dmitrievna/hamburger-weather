package com.project.hamburger_weather.presentation;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.hamburger_weather.application.RouteAndWeatherService;
import com.project.hamburger_weather.domain.model.Address;
import com.project.hamburger_weather.presentation.dto.ForecastRequestDto;
import com.project.hamburger_weather.presentation.dto.ForecastResponseDto;
import com.project.hamburger_weather.presentation.mapper.WeatherReportMapper;

import reactor.core.publisher.Mono;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/weather")
@Validated
public class WeatherController {

    private final RouteAndWeatherService routeAndWeatherService;
    private final WeatherReportMapper weatherReportMapper;

    public WeatherController(RouteAndWeatherService routeAndWeatherService, WeatherReportMapper weatherReportMapper) {
        this.routeAndWeatherService = routeAndWeatherService;
        this.weatherReportMapper = weatherReportMapper;
    }

    @PostMapping("/forecast-based-on-route")
    public Mono<ForecastResponseDto> requestRouteAndWeather(@RequestBody @Valid ForecastRequestDto request) {
        Address from = new Address(request.start().street(), request.start().num(), request.start().plz(),
                request.start().city(), request.start().country());
        Address to = new Address(request.end().street(), request.end().num(), request.end().plz(),
                request.end().city(), request.end().country());

        Mono<ForecastResponseDto> response = routeAndWeatherService.getRouteForecastResultForGivenStartAndFinish(from, to)
                .map(r -> weatherReportMapper.toForecastResponseDto(r.forecast(), r.route()));

        return response;
    }

}
