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
import com.project.hamburger_weather.presentation.mapper.ForecastResponseMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.project.hamburger_weather.presentation.mapper.AddressMapper;
import org.springframework.http.ResponseEntity;
import com.project.hamburger_weather.exception.InvalidRouteException;

import reactor.core.publisher.Mono;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/weather")
@Validated
@Tag(name = "Route & Weather", description = "Get weather forecast and safety assessment for a cycling route")
public class WeatherAndAccidentsAssessmentController {

    private final WeatherAndAccidentService weatherAndAccidentService;
    private final ForecastResponseMapper weatherReportMapper;
    private final AddressMapper addressMapper;

    public WeatherAndAccidentsAssessmentController(WeatherAndAccidentService weatherAndAccidentService, ForecastResponseMapper weatherReportMapper, AddressMapper addressMapper) {
        this.weatherAndAccidentService = weatherAndAccidentService;
        this.weatherReportMapper = weatherReportMapper;
        this.addressMapper = addressMapper;
    }

    @Operation(
            summary = "Get route forecast",
            description = "Calculates a cycling route between two addresses and returns route, weather forecast and accident risk assessment"
    )
    @ApiResponse(responseCode = "200", description = "Forecast successfully calculated")
    @ApiResponse(responseCode = "400", description = "Invalid address format")
    @ApiResponse(responseCode = "503", description = "External API unavailable")
    @PostMapping("/forecast-based-on-route")
    public Mono<ResponseEntity<ForecastResponseDto>> requestRouteAndWeather(@RequestBody @Valid ForecastRequestDto request) {
        Address from = addressMapper.toDomain(request.startAddress());
        Address to = addressMapper.toDomain(request.endAddress());

        return weatherAndAccidentService.getForecast(from, to, request.tag())
                .map(r -> ResponseEntity.ok().body(weatherReportMapper.toForecastResponseDto(r)))
                .onErrorResume(InvalidRouteException.class,
                        e -> Mono.just(ResponseEntity.badRequest().build()));

    }

}
