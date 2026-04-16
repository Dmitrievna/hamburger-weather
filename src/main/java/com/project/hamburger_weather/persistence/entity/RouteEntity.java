package com.project.hamburger_weather.persistence.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import java.time.LocalDateTime;

@Table("route_requests")
public class RouteEntity {

    @Id
    private Long id;

    private String tag;

    // start address 
    private String startStreet;
    private String startHouseNumber;
    private String startPostalCode;
    private String startCity;
    private String startCountry;

    // end address 
    private String endStreet;
    private String endHouseNumber;
    private String endPostalCode;
    private String endCity;
    private String endCountry;

    // coordinates as JSON
    private String coordinates;

    private LocalDateTime requestedAt;

    public RouteEntity() {
    }

    public RouteEntity(String tag, String startStreet, String startHouseNumber, String startPostalCode, String startCity, String startCountry,
            String endStreet, String endHouseNumber, String endPostalCode, String endCity, String endCountry,
            String coordinates, LocalDateTime requestedAt) {
        this.tag = tag;
        this.startStreet = startStreet;
        this.startHouseNumber = startHouseNumber;
        this.startPostalCode = startPostalCode;
        this.startCity = startCity;
        this.startCountry = startCountry;
        this.endStreet = endStreet;
        this.endHouseNumber = endHouseNumber;
        this.endPostalCode = endPostalCode;
        this.endCity = endCity;
        this.endCountry = endCountry;
        this.coordinates = coordinates;
        this.requestedAt = requestedAt;
    }

    public Long getId() {
        return id;
    }

    public String getTag() {
        return tag;
    }

    public String getStartStreet() {
        return startStreet;
    }

    public String getStartHouseNumber() {
        return startHouseNumber;
    }

    public String getStartPostalCode() {
        return startPostalCode;
    }

    public String getStartCity() {
        return startCity;
    }

    public String getStartCountry() {
        return startCountry;
    }

    public String getEndStreet() {
        return endStreet;
    }

    public String getEndHouseNumber() {
        return endHouseNumber;
    }

    public String getEndPostalCode() {
        return endPostalCode;
    }

    public String getEndCity() {
        return endCity;
    }

    public String getEndCountry() {
        return endCountry;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public LocalDateTime getRequestedAt() {
        return requestedAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setStartStreet(String startStreet) {
        this.startStreet = startStreet;
    }

    public void setStartHouseNumber(String startHouseNumber) {
        this.startHouseNumber = startHouseNumber;
    }

    public void setStartPostalCode(String startPostalCode) {
        this.startPostalCode = startPostalCode;
    }

    public void setStartCity(String startCity) {
        this.startCity = startCity;
    }

    public void setStartCountry(String startCountry) {
        this.startCountry = startCountry;
    }

    public void setEndStreet(String endStreet) {
        this.endStreet = endStreet;
    }

    public void setEndHouseNumber(String endHouseNumber) {
        this.endHouseNumber = endHouseNumber;
    }

    public void setEndPostalCode(String endPostalCode) {
        this.endPostalCode = endPostalCode;
    }

    public void setEndCity(String endCity) {
        this.endCity = endCity;
    }

    public void setEndCountry(String endCountry) {
        this.endCountry = endCountry;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

    public void setRequestedAt(LocalDateTime requestedAt) {
        this.requestedAt = requestedAt;
    }

}
