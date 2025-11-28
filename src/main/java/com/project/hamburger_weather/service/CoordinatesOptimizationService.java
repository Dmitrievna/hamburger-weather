package com.project.hamburger_weather.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.project.hamburger_weather.dto.CoordinatesDto;
import com.project.hamburger_weather.dto.RouteDto;

@Service
public class CoordinatesOptimizationService {


    public RouteDto deduplicate(List<CoordinatesDto> coordinatesList, double thresholdKm) {
        List<CoordinatesDto> optimizedList = new ArrayList<>();
        System.out.println("before optimization: " + coordinatesList.size());
        for (CoordinatesDto coord : coordinatesList) {
            
            if(optimizedList.stream().noneMatch(
                c -> distanceInKm(c.lat(), c.lon(), coord.lat(), coord.lon()) < thresholdKm
            )) {
                optimizedList.add(coord);
            }
        }
        System.out.println("after optimization: " + optimizedList.size());
        return new RouteDto(optimizedList);
    }


    private static double distanceInKm(String lat1, String lon1, String lat2, String lon2) {
        double R = 6371; // Radius of the earth in km
        double dLat = Math.toRadians(Double.parseDouble(lat2) - Double.parseDouble(lat1));
        double dLon = Math.toRadians(Double.parseDouble(lon2) - Double.parseDouble(lon1));
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(Double.parseDouble(lat1))) * Math.cos(Math.toRadians(Double.parseDouble(lat2)))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2); // haversine formula
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c; // Distance in km
    }
}
