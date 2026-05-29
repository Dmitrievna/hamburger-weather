package com.project.hamburger_weather.infra.persistence.mapper;

import com.project.hamburger_weather.domain.model.SavedRoute;
import com.project.hamburger_weather.infra.persistence.entity.RouteEntity;
import org.springframework.stereotype.Component;
import com.project.hamburger_weather.domain.model.Address;
import com.google.gson.Gson;
import java.util.List;
import com.project.hamburger_weather.domain.model.Coordinate;
import java.time.LocalDateTime;
import com.google.gson.reflect.TypeToken;
import org.springframework.lang.NonNull;

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

    @NonNull
    public RouteEntity toEntity(String tag, Address from, Address to, List<Coordinate> route) {
        String coordinatesJson = new Gson().toJson(route);
        return RouteEntity.builder()
                .tag(tag)
                .startStreet(from.getStreet())
                .startHouseNumber(from.getHouseNumber())
                .startPlz(from.getPlz())
                .startCity(from.getCity())
                .startCountry(from.getCountry())
                .endStreet(to.getStreet())
                .endHouseNumber(to.getHouseNumber())
                .endPlz(to.getPlz())
                .endCity(to.getCity())
                .endCountry(to.getCountry())
                .coordinates(new Gson().toJson(route))
                .requestedAt(LocalDateTime.now())
                .build();
    }

    private List<Coordinate> parseCoordinates(String coordinatesJson) {
        return new Gson().fromJson(
                coordinatesJson,
                new TypeToken<List<Coordinate>>() {
                }.getType()
        );
    }
}
