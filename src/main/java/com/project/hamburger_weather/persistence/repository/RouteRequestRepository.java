package com.project.hamburger_weather.persistence.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.project.hamburger_weather.persistence.entity.RouteEntity;

import reactor.core.publisher.Mono;

import com.project.hamburger_weather.domain.model.Address;

@Repository
public interface RouteRequestRepository
        extends ReactiveCrudRepository<RouteEntity, Long> {

    Mono<RouteEntity> findByTag(String tag);

    Mono<RouteEntity> findByAddresses(Address start, Address end);

}
