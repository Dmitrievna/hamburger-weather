package com.project.hamburger_weather.dto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;    

public class HourlyForecastDto {
    private final Date hour;
    private final String temperature;
    private final String precipitationProbability;
    private final String precipitation;

    private static final SimpleDateFormat timeFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

    public HourlyForecastDto(String hour, String temperature, String precipitationProbability, String precipitation) throws ParseException {
        this.hour = timeFormatter.parse(hour);
        this.temperature = temperature;
        this.precipitationProbability = precipitationProbability;
        this.precipitation = precipitation;
    }

    public String getAll() {
        return "Hour: " + hour + ", Temp: " + temperature + ", PrecipProb: " + precipitationProbability + ", Precip: " + precipitation;
    }


}
