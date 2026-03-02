package com.project.hamburger_weather.presentation.dto;

import java.time.LocalDateTime;

public record ErrorResponse(LocalDateTime timestamp, String error, String message) {

}
