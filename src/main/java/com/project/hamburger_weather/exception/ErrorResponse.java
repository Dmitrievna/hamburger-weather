package com.project.hamburger_weather.exception;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public record ErrorResponse(
        LocalDateTime timestamp,
        String error,
        List<String> details
        ) {

}
