package com.project.hamburger_weather.service;


/* 
@Service
public class CoordinatesOptimizationService {


    public RoutingServiceDto deduplicate(List<CoordinateDto> coordinatesList, double thresholdKm) {
        List<CoordinateDto> optimizedList = new ArrayList<>();
        System.out.println("before optimization: " + coordinatesList.size());
        for (CoordinateDto coord : coordinatesList) {
            
            if(optimizedList.stream().noneMatch(
                c -> distanceInKm(c.lat(), c.lon(), coord.lat(), coord.lon()) < thresholdKm
            )) {
                optimizedList.add(coord);
            }
        }
        System.out.println("after optimization: " + optimizedList.size());
        return new RoutingServiceDto(optimizedList);
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
 */
