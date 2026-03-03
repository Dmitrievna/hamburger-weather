package com.project.hamburger_weather.infra.support;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.project.hamburger_weather.domain.model.Coordinate;
import com.project.hamburger_weather.domain.model.Route;

@Component
public class CoordinatesOptimizator {

    private static final double DEFAULT_THRESHOLD_KM = 1.0; // Default threshold of 1 km

    public CoordinatesOptimizator() {
    }

    public Route deduplicate(Route route) {
        return deduplicate(route, DEFAULT_THRESHOLD_KM);
    }

    private Route deduplicate(Route route, double thresholdKm) {
        List<Coordinate> optimizedRoute = new ArrayList<>();
        // always include the first and the last coordinate i.e. starting and finishing routing point
        optimizedRoute.add(route.coordinates().get(0));
        for (int i = 1; i < route.coordinates().size() - 1; i++) {

            Coordinate coord = route.coordinates().get(i);

            if (optimizedRoute.stream().noneMatch(
                    c -> distanceInKm(c.latitude(), c.longitude(), coord.latitude(), coord.longitude()) < thresholdKm
            )) {
                if (!optimizedRoute.contains(coord)) {
                    optimizedRoute.add(coord);
                }

            }
        }

        optimizedRoute.add(route.coordinates().get(route.coordinates().size() - 1));
        return new Route(optimizedRoute);
    }

    private static double distanceInKm(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371; // Radius of the earth in km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2); // haversine formula
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c; // Distance in km
    }
}
