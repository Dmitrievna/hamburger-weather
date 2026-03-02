package com.project.hamburger_weather.infra.api.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.project.hamburger_weather.domain.model.Coordinate;
import com.project.hamburger_weather.domain.model.Route;
import com.project.hamburger_weather.presentation.dto.RouteDto;
import com.project.hamburger_weather.presentation.dto.RoutingServiceDto;

@Component
public class RoutingServiceMapper {

    public Route toRoute(RoutingServiceDto dto) {
        RouteDto firstRoute = dto.routes().get(0);

        List<Coordinate> coordinates
                = firstRoute.geometry().coordinates().stream()
                        .map(pair -> new Coordinate(
                        pair.get(0), // lon
                        pair.get(1) // lat
                ))
                        .toList();

        return new Route(
                coordinates
        );
    }
}
