package com.project.hamburger_weather.persistence.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import java.time.LocalDateTime;

@Table("route_requests")
public class RouteRequestEntity {

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
}
