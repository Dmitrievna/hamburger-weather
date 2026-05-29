package com.project.hamburger_weather.exception;

public class InvalidRouteException extends RuntimeException {

    public InvalidRouteException(String message) {
        super(message);
    }

    public InvalidRouteException(String message, Throwable cause) {
        super(message, cause);
    }

}
