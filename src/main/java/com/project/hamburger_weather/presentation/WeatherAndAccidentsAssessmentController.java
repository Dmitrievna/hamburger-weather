package com.project.hamburger_weather.presentation;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.hamburger_weather.application.WeatherAndAccidentService;
import com.project.hamburger_weather.domain.model.Address;
import com.project.hamburger_weather.presentation.dto.ForecastRequestDto;
import com.project.hamburger_weather.presentation.dto.ForecastResponseDto;
import com.project.hamburger_weather.presentation.mapper.WeatherReportMapper;
import com.project.hamburger_weather.presentation.mapper.AddressMapper;
import org.springframework.http.ResponseEntity;
import com.project.hamburger_weather.exception.InvalidRouteException;

import reactor.core.publisher.Mono;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/weather")
@Validated
public class WeatherAndAccidentsAssessmentController {

    private final WeatherAndAccidentService weatherAndAccidentService;
    private final WeatherReportMapper weatherReportMapper;
    private final AddressMapper addressMapper;

    public WeatherAndAccidentsAssessmentController(WeatherAndAccidentService weatherAndAccidentService, WeatherReportMapper weatherReportMapper, AddressMapper addressMapper) {
        this.weatherAndAccidentService = weatherAndAccidentService;
        this.weatherReportMapper = weatherReportMapper;
        this.addressMapper = addressMapper;
    }

    @PostMapping("/forecast-based-on-route")
    public Mono<ResponseEntity<ForecastResponseDto>> requestRouteAndWeather(@RequestBody @Valid ForecastRequestDto request) {
        Address from = addressMapper.toDomain(request.start());
        Address to = addressMapper.toDomain(request.end());

        return weatherAndAccidentService.getForecast(from, to, request.tag())
                .map(r -> ResponseEntity.ok().body(weatherReportMapper.toForecastResponseDto(r)))
                .onErrorResume(InvalidRouteException.class,
                        e -> Mono.just(ResponseEntity.badRequest().build()));

    }

}
