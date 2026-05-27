package com.project.hamburger_weather.domain.model;

public record AccidentReport(
        int totalAccidents,
        int clearAccidents,
        int twilightAccidents,
        int nightAccidents,
        int dryAccidents,
        int rainyAccidents,
        int snowyAccidents,
        RiskLevel riskLevel
        ) {

}
