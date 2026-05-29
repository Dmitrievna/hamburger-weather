package com.project.hamburger_weather.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.hamburger_weather.application.RouteResolutionService;
import com.project.hamburger_weather.presentation.dto.RouteResponseDto;

import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/routes")
@Validated
public class RouteManagingController {

    private final RouteResolutionService savedRouteService;

    public RouteManagingController(RouteResolutionService savedRouteService) {
        this.savedRouteService = savedRouteService;
    }

    //make here possible to see the route and also to change the tag and only tag
    /*
    @GetMapping("/{tag}")
    public Mono<ResponseEntity<RouteResponseDto>> getRouteByTag(@RequestParam @Valid String tag) {
        // Implementation to retrieve a route by its Tag

        /// todo check exceptions if not found should work
        return savedRouteService.getRouteByTag(tag)
                .map(route -> ResponseEntity.ok(new RouteResponseDto(
                route.tag(),
                route.startAddress(),
                route.endAddress(),
                route.coordinates(),
                route.requestedAt().toString()
        )))
                .defaultIfEmpty(ResponseEntity.notFound().build());

    } */
}
