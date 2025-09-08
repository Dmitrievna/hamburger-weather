package com.project.hamburger_weather.exception;

public class RouteParsingException extends RuntimeException {

    public RouteParsingException(String message) {
        super(message);
    }

    public RouteParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}
