package com.project.hamburger_weather.infra.persistence.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.project.hamburger_weather.infra.persistence.entity.RouteEntity;

import reactor.core.publisher.Mono;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface RouteRequestRepository
        extends ReactiveCrudRepository<RouteEntity, Long> {

    Mono<Boolean> existsByTag(String tag);

    @Query("SELECT EXISTS (SELECT 1 FROM route_requests WHERE start_street = :startStreet AND start_city = :startCity AND start_country = :startCountry AND end_street = :endStreet AND end_city = :endCity AND end_country = :endCountry)")
    Mono<Boolean> existsByAddresses(
            @Param("startStreet") String startStreet,
            @Param("startCity") String startCity,
            @Param("startCountry") String startCountry,
            @Param("endStreet") String endStreet,
            @Param("endCity") String endCity,
            @Param("endCountry") String endCountry);

    Mono<RouteEntity> findByTag(String tag);

    @Query("SELECT * FROM route_requests r WHERE r.start_street = :startStreet AND r.start_city = :startCity AND r.start_country = :startCountry AND r.end_street = :endStreet AND r.end_city = :endCity AND r.end_country = :endCountry")
    Mono<RouteEntity> findByAddresses(
            @Param("startStreet") String startStreet,
            @Param("startCity") String startCity,
            @Param("startCountry") String startCountry,
            @Param("endStreet") String endStreet,
            @Param("endCity") String endCity,
            @Param("endCountry") String endCountry);

}
