package com.project.hamburger_weather.infra.persistence.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import lombok.Data;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("route_requests")
public class RouteEntity {

    @Id
    private Long id;

    private String tag;

    // start address 
    private String startStreet;
    private String startHouseNumber;
    private String startPlz;
    private String startCity;
    private String startCountry;

    // end address 
    private String endStreet;
    private String endHouseNumber;
    private String endPlz;
    private String endCity;
    private String endCountry;

    // coordinates as JSON
    private String coordinates;

    private LocalDateTime requestedAt;
}
