package com.project.hamburger_weather.domain.model;

import lombok.Setter;
import lombok.Getter;

@Getter
@Setter
public class Address {

    private String street;
    private String num;
    private String city;
    private String plz;
    private String country;

    public Address(String street, String num, String city, String plz, String country) {
        if (street == null || street.isEmpty()) {
            throw new IllegalArgumentException("Street cannot be null or empty");
        }
        if (num == null || num.isEmpty()) {
            throw new IllegalArgumentException("Number cannot be null or empty");
        }
        if (city == null || city.isEmpty()) {
            throw new IllegalArgumentException("City cannot be null or empty");
        }
        if (plz == null || plz.isEmpty()) {
            throw new IllegalArgumentException("Postal code cannot be null or empty");
        }
        if (country == null || country.isEmpty()) {
            throw new IllegalArgumentException("Country cannot be null or empty");
        }
        this.street = street;
        this.num = num;
        this.city = city;
        this.plz = plz;
        this.country = country;
    }

}
