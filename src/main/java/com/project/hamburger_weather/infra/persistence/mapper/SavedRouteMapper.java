package com.project.hamburger_weather.infra.persistence.mapper;

import com.project.hamburger_weather.domain.model.SavedRoute;
import com.project.hamburger_weather.infra.persistence.entity.RouteEntity;
import org.springframework.stereotype.Component;
import com.project.hamburger_weather.domain.model.Address;
import com.project.hamburger_weather.domain.model.Route;
import com.google.gson.Gson;
import java.util.List;
import com.project.hamburger_weather.domain.model.Coordinate;
import java.time.LocalDateTime;
import com.google.gson.reflect.TypeToken;

@Component
public class SavedRouteMapper {

    public SavedRoute toDomain(RouteEntity entity) {
        return new SavedRoute(
                entity.getTag(),
                new Address(
                        entity.getStartStreet(),
                        entity.getStartHouseNumber(),
                        entity.getStartPlz(),
                        entity.getStartCity(),
                        entity.getStartCountry()
                ),
                new Address(
                        entity.getEndStreet(),
                        entity.getEndHouseNumber(),
                        entity.getEndPlz(),
                        entity.getEndCity(),
                        entity.getEndCountry()
                ),
                parseCoordinates(entity.getCoordinates()),
                entity.getRequestedAt()
        );
    }

    public RouteEntity toEntity(String tag, Address from, Address to, Route route) {
        String coordinatesJson = new Gson().toJson(route.coordinates());
        System.out.println("Coordinates JSON: " + coordinatesJson); // add this
        System.out.println("Coordinates type: " + coordinatesJson.getClass().getName());
        return RouteEntity.builder()
                .tag(tag)
                .startStreet(from.street())
                .startHouseNumber(from.num())
                .startPlz(from.plz())
                .startCity(from.city())
                .startCountry(from.country())
                .endStreet(to.street())
                .endHouseNumber(to.num())
                .endPlz(to.plz())
                .endCity(to.city())
                .endCountry(to.country())
                .coordinates(new Gson().toJson(route.coordinates()))
                .requestedAt(LocalDateTime.now())
                .build();
    }

    private Route parseCoordinates(String coordinatesJson) {
        List<Coordinate> coordinates = new Gson().fromJson(
                coordinatesJson,
                new TypeToken<List<Coordinate>>() {
                }.getType()
        );
        return new Route(coordinates);
    }
}
