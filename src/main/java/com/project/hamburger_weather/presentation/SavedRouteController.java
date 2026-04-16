package com.project.hamburger_weather.presentation;

import com.project.hamburger_weather.presentation.dto.RouteDto;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import com.project.hamburger_weather.domain.model.SavedRoute;
import com.project.hamburger_weather.domain.service.SavedRouteService;
import com.project.hamburger_weather.presentation.dto.RouteResponseDto;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/v1/routes")
public class SavedRouteController {

    private final SavedRouteService savedRouteService;

    public SavedRouteController(SavedRouteService savedRouteService) {
        this.savedRouteService = savedRouteService;
    }

    //get all saved routes
    //get a specific route by id
    @GetMapping("/{tag}")
    public Mono<ResponseEntity<RouteResponseDto>> getRouteByTag(@RequestParam String tag) {
        // Implementation to retrieve a route by its Tag

        /// todo check exceptions if not found should work
        return savedRouteService.getRouteByTag(tag)
                .map(route -> ResponseEntity.ok(new RouteResponseDto(
                route.tag(),
                route.startAddress(),
                route.endAddress(),
                route.route(),
                route.requestedAt().toString()
        )))
                .defaultIfEmpty(ResponseEntity.notFound().build());

    }
    //save a route
    //delete a route
}
