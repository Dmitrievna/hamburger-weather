package com.project.hamburger_weather.application;

import org.springframework.stereotype.Service;
import com.project.hamburger_weather.infra.loader.AccidentDataLoader;

@Service
public class AccidentDataService {

    private final AccidentDataLoader accidentDataLoader;

    public AccidentDataService(AccidentDataLoader accidentDataLoader) {
        this.accidentDataLoader = accidentDataLoader;
    }

    public int getNumberOfAccidents() {
        return accidentDataLoader.getAccidentStats() == null ? 0 : accidentDataLoader.getAccidentStats().size();
    }
}
