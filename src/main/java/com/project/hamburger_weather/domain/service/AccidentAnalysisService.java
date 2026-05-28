package com.project.hamburger_weather.domain.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.hamburger_weather.domain.model.AccidentReport;
import com.project.hamburger_weather.domain.model.AccidentStats;
import com.project.hamburger_weather.domain.model.Coordinate;
import com.project.hamburger_weather.domain.model.RiskLevel;
import com.project.hamburger_weather.infra.loader.AccidentDataLoader;
import com.project.hamburger_weather.infra.support.CoordinateOptimizerHelper;

@Service
public class AccidentAnalysisService {

    private final AccidentDataLoader accidentDataLoader;

    private static final double RISK_RADIUS_KM = 0.1; // radius to consider for nearby accidents

    public AccidentAnalysisService(AccidentDataLoader accidentDataLoader) {
        this.accidentDataLoader = accidentDataLoader;
    }

    public AccidentReport getRoadAccidentsAssessment(List<Coordinate> coord) {
        // calculate radius of accidents around a route
        // calculate accident risk for a route

        List<AccidentStats> accidents = accidentDataLoader.getAccidentStats();

        int totalAccidents = 0;
        int clearAccidents = 0;
        int twilightAccidents = 0;
        int nightAccidents = 0;
        int dryAccidents = 0;
        int rainyAccidents = 0;
        int snowyAccidents = 0;

        for (Coordinate c : coord) {
            for (AccidentStats a : accidents) {
                if (CoordinateOptimizerHelper.distanceInKm(c, a.coordinate()) <= RISK_RADIUS_KM) {
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
