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
                        entity.getStartCity(),
                        entity.getStartPlz(),
                        entity.getStartCountry()
                ),
                new Address(
                        entity.getEndStreet(),
                        entity.getEndHouseNumber(),
                        entity.getEndCity(),
                        entity.getEndPlz(),
                        entity.getEndCountry()
                ),
                parseCoordinates(entity.getCoordinates()),
                entity.getRequestedAt()
        );
    }

    @NonNull
    public RouteEntity toEntity(String tag, Address from, Address to, List<Coordinate> route) {
        String coordinatesJson = new Gson().toJson(route);
        RouteEntity entity = new RouteEntity();
        entity.setTag(tag);
        entity.setStartStreet(from.getStreet());
        entity.setStartHouseNumber(from.getNum());
        entity.setStartPlz(from.getPlz());
        entity.setStartCity(from.getCity());
        entity.setStartCountry(from.getCountry());
        entity.setEndStreet(to.getStreet());
        entity.setEndHouseNumber(to.getNum());
        entity.setEndPlz(to.getPlz());
        entity.setEndCity(to.getCity());
        entity.setEndCountry(to.getCountry());
        entity.setCoordinates(coordinatesJson);
        entity.setRequestedAt(LocalDateTime.now());
        return entity;
    }

    private List<Coordinate> parseCoordinates(String coordinatesJson) {
        return new Gson().fromJson(
                coordinatesJson,
                new TypeToken<List<Coordinate>>() {
                }.getType()
        );
    }
}
