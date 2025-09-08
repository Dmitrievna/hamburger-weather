package com.project.hamburger_weather.dto;

import java.util.List;

public record WeatherForecastDto(
    CoordinatesDto coordinates,
    List<HourlyForecastDto> hourlyForecast
) {}


//JSON response example:
// {
//     "latitude":53.58,
//     "longitude":10.059999,
//     "generationtime_ms":0.052809715270996094,
//     "utc_offset_seconds":7200,
//     "timezone":"Europe/Berlin",
//     "timezone_abbreviation":"GMT+2",
//     "elevation":19.0,
//     "hourly_units":
//         {
//             "time":"iso8601",
//             "temperature_2m":"°C",
//             "precipitation_probability":"%",
//             "precipitation":"mm"
//         },
//     "hourly":
//         {"time":
//         ["2025-09-03T11:00","2025-09-03T12:00","2025-09-03T13:00"],
//         "temperature_2m":[19.4,21.3,22.0],
//         "precipitation_probability":[0,0,0],
//         "precipitation":[0.00,0.00,0.00]
//     }}
