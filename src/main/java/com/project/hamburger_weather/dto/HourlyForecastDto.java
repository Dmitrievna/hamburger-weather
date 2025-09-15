package com.project.hamburger_weather.dto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;    

public class HourlyForecastDto {
    private final Date hour;
    private final String temperature;
    private final String precipitationProbability;
    private final String precipitation;
    private final String windSpeed;
    private final String weatherCode;

    private static final SimpleDateFormat timeFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

    public HourlyForecastDto(String hour, String temperature, String precipitationProbability, String precipitation, String windSpeed, String weatherCode) throws ParseException {
        this.hour = timeFormatter.parse(hour);
        this.temperature = temperature;
        this.precipitationProbability = precipitationProbability;
        this.precipitation = precipitation;
        this.windSpeed = windSpeed;
        this.weatherCode = weatherCode;
    }

    public String getAll() {
        return "Hour: " + hour + ", Temp: " + temperature + ", PrecipProb: " + precipitationProbability + ", Precip: " + precipitation + ", WindSpeed: " + windSpeed + ", WeatherCode: " + weatherCode;
    }


}
