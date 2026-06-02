package com.project.hamburger_weather.domain.validation;

import com.project.hamburger_weather.domain.model.Address;
import com.project.hamburger_weather.exception.InvalidRouteException;

public class RouteValidator {

    public static void validateRouteAddresses(Address from, Address to) {
        if (from.equals(to)) {
            throw new InvalidRouteException("Start and end address must differ");
        }
    }
}
