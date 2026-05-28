package com.project.hamburger_weather.infra.support;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.project.hamburger_weather.domain.model.Coordinate;

@Component
public class CoordinateOptimizerHelper {

    private static final double DEFAULT_THRESHOLD_KM = 1.0; // Default threshold of 1 km

    public CoordinateOptimizerHelper() {
    }

    public List<Coordinate> deduplicate(List<Coordinate> listOfCoordinates) {
        return deduplicate(listOfCoordinates, DEFAULT_THRESHOLD_KM);
    }

    private List<Coordinate> deduplicate(List<Coordinate> listOfCoordinates, double thresholdKm) {
        List<Coordinate> optimizedRoute = new ArrayList<>();
        // always include the first and the last coordinate i.e. starting and finishing routing point
        optimizedRoute.add(listOfCoordinates.get(0));
        for (int i = 1; i < listOfCoordinates.size() - 1; i++) {

            Coordinate coord = listOfCoordinates.get(i);

            if (optimizedRoute.stream().noneMatch(
                    c -> distanceInKm(c, coord) < thresholdKm
            )) {
                if (!optimizedRoute.contains(coord)) {
                    optimizedRoute.add(coord);
                }

            }
        }

        optimizedRoute.add(listOfCoordinates.get(listOfCoordinates.size() - 1));
        return optimizedRoute;
    }

    public static double distanceInKm(Coordinate coord1, Coordinate coord2) {
        double R = 6371; // Radius of the earth in km
        double lat1 = coord1.latitude();
        double lon1 = coord1.longitude();
        double lat2 = coord2.latitude();
        double lon2 = coord2.longitude();
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2); // haversine formula
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c; // Distance in km
    }
}
