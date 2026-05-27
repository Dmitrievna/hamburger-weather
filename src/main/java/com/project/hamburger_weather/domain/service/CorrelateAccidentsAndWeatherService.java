package com.project.hamburger_weather.domain.service;

import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import com.project.hamburger_weather.domain.model.AccidentReport;
import com.project.hamburger_weather.domain.model.ForecastReport;
import com.project.hamburger_weather.domain.model.LightCondition;

@Service
public class CorrelateAccidentsAndWeatherService {

    public static int correlateWeatherAndAccidents(ForecastReport forecastReport, AccidentReport accidentReport) {
        // correlate weather and accidents for a given route

        int weatherRelatedAccidents = 0;
        int time = LocalDateTime.now().getHour();
        LightCondition currentLightCondition = lightConditionEvaluationSummer(time);

        if (forecastReport.rainy() && accidentReport.rainyAccidents() > 0) {
            weatherRelatedAccidents += accidentReport.rainyAccidents();
        } else if (forecastReport.snowy() && accidentReport.snowyAccidents() > 0) {
            weatherRelatedAccidents += accidentReport.snowyAccidents();
        } else if (forecastReport.goodWeather() && accidentReport.clearAccidents() > 0) {
            weatherRelatedAccidents += accidentReport.clearAccidents();
        }

        if (currentLightCondition == LightCondition.CLEAR && accidentReport.clearAccidents() > 0) {
            weatherRelatedAccidents += accidentReport.clearAccidents();
        } else if (currentLightCondition == LightCondition.TWILIGHT && accidentReport.twilightAccidents() > 0) {
            weatherRelatedAccidents += accidentReport.twilightAccidents();
        } else if (currentLightCondition == LightCondition.NIGHT && accidentReport.nightAccidents() > 0) {
            weatherRelatedAccidents += accidentReport.nightAccidents();
        }

        return weatherRelatedAccidents;

    }

    private static LightCondition lightConditionEvaluationSummer(int time) {
        if (time >= 6 && time < 18) {
            return LightCondition.CLEAR;
        } else if (time >= 18 && time < 22) {
            return LightCondition.TWILIGHT;
        } else {
            return LightCondition.NIGHT;
        }
    }
}
