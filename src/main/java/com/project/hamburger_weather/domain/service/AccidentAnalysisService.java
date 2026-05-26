package com.project.hamburger_weather.domain.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.project.hamburger_weather.domain.model.Coordinate;
import com.project.hamburger_weather.domain.model.SegmentsRisk;
import com.project.hamburger_weather.infra.loader.AccidentDataLoader;

@Service
public class AccidentAnalysisService {

    private final AccidentDataLoader accidentDataLoader;

    private static final double RISK_RADIUS_KM = 0.1; // radius to consider for nearby accidents

    public AccidentAnalysisService(AccidentDataLoader accidentDataLoader) {
        this.accidentDataLoader = accidentDataLoader;
    }

    List<SegmentsRisk> getNearbyAccidents(List<Coordinate> route) {
        // calculate radius of accidents around a route
        // calculate accident risk for a route

        //first calculate radius so we can sort out the accidents
        // then calculate risk based on number of accidents
        // somehow we need ti take into account the weather condition 
        // and lights?
    }

}
