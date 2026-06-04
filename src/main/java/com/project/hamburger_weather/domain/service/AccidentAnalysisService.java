package com.project.hamburger_weather.domain.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.hamburger_weather.domain.model.AccidentReport;
import com.project.hamburger_weather.domain.model.AccidentStats;
import com.project.hamburger_weather.domain.model.Coordinate;
import com.project.hamburger_weather.domain.model.RiskLevel;
import com.project.hamburger_weather.infra.loader.AccidentDataLoader;
import com.project.hamburger_weather.infra.support.CoordinateOptimizer;

@Service
public class AccidentAnalysisService {

    private final AccidentDataLoader accidentDataLoader;

    private static final Double RISK_RADIUS_KM = 0.1; // radius to consider for nearby accidents

    public AccidentAnalysisService(AccidentDataLoader accidentDataLoader) {
        this.accidentDataLoader = accidentDataLoader;
    }

    public AccidentReport getRoadAccidentsAssessment(List<Coordinate> coord) {
        // calculate radius of accidents around a route
        // calculate accident risk for a route

        //to remove List<AccidentStats> accidents;
        int totalAccidents = 0;
        int clearAccidents = 0;
        int twilightAccidents = 0;
        int nightAccidents = 0;
        int dryAccidents = 0;
        int rainyAccidents = 0;
        int snowyAccidents = 0;

        for (Coordinate c : coord) {
            List<AccidentStats> nearby = accidentDataLoader.findNearby(c.latitude(), c.longitude(), RISK_RADIUS_KM);

            for (AccidentStats a : nearby) {
                if (CoordinateOptimizer.distanceInKm(c, a.coordinate()) <= RISK_RADIUS_KM) {

                    switch (a.lightCondition()) {
                        case CLEAR ->
                            clearAccidents++;
                        case TWILIGHT ->
                            twilightAccidents++;
                        case NIGHT ->
                            nightAccidents++;
                    }

                    switch (a.roadCondition()) {
                        case DRY ->
                            dryAccidents++;
                        case RAIN ->
                            rainyAccidents++;
                        case SNOW ->
                            snowyAccidents++;
                    }
                    totalAccidents++;
                }
            }
            return new AccidentReport(
                    totalAccidents,
                    clearAccidents,
                    twilightAccidents,
                    nightAccidents,
                    dryAccidents,
                    rainyAccidents,
                    snowyAccidents,
                    calculateRiskLevel(totalAccidents)
            );

        };
        /* for (Coordinate c : coord) {
            for (AccidentStats a : accidents) {
                if (CoordinateOptimizer.distanceInKm(c, a.coordinate()) <= RISK_RADIUS_KM) {
                    switch (a.lightCondition()) {
                        case CLEAR ->
                            clearAccidents++;
                        case TWILIGHT ->
                            twilightAccidents++;
                        case NIGHT ->
                            nightAccidents++;
                    }

                    switch (a.roadCondition()) {
                        case DRY ->
                            dryAccidents++;
                        case RAIN ->
                            rainyAccidents++;
                        case SNOW ->
                            snowyAccidents++;
                    }
                    totalAccidents++;
                }
            }*/
        //}
        return new AccidentReport(
                totalAccidents,
                clearAccidents,
                twilightAccidents,
                nightAccidents,
                dryAccidents,
                rainyAccidents,
                snowyAccidents,
                calculateRiskLevel(totalAccidents)
        );
    }

    public List<AccidentStats> findNearbyAccidents(List<Coordinate> routeCoordinates) {
        return routeCoordinates.stream()
                .flatMap(coord -> accidentDataLoader
                .findNearby(coord.latitude(), coord.longitude(), RISK_RADIUS_KM)
                .stream())
                .distinct()
                .toList();
    }

    private RiskLevel calculateRiskLevel(int totalAccidents) {
        if (totalAccidents >= 3) {
            return RiskLevel.HIGH;
        } else if (totalAccidents >= 1) {
            return RiskLevel.MEDIUM;
        } else {
            return RiskLevel.LOW;
        }
    }

}
