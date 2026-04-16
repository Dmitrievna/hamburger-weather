package com.project.hamburger_weather.persistence.mapper;

import com.project.hamburger_weather.domain.model.SavedRoute;
import com.project.hamburger_weather.persistence.entity.RouteEntity;
import org.springframework.stereotype.Component;
import com.project.hamburger_weather.domain.model.Address;
import com.project.hamburger_weather.domain.model.Route;
import com.google.gson.Gson;
import java.util.List;
import com.project.hamburger_weather.domain.model.Coordinate;

@Component
public class SavedRouteMapper {

    public SavedRoute toDomain(RouteEntity entity) {
        return new SavedRoute(
                entity.getTag(),
                new Address(
                        entity.getStartStreet(),
                        entity.getStartHouseNumber(),
                        entity.getStartPostalCode(),
                        entity.getStartCity(),
                        entity.getStartCountry()
                ),
                new Address(
                        entity.getEndStreet(),
                        entity.getEndHouseNumber(),
                        entity.getEndPostalCode(),
                        entity.getEndCity(),
                        entity.getEndCountry()
                ),
                parseCoordinates(entity.getCoordinates()),
                entity.getRequestedAt()
        );
    }

    private Route parseCoordinates(String coordinatesJson) {

        Coordinate[] coordinates = new Gson().fromJson(coordinatesJson, Coordinate[].class);
        return new Route(List.of(coordinates));
    }
}
