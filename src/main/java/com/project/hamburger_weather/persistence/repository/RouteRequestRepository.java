package com.project.hamburger_weather.persistence.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import com.project.hamburger_weather.persistence.entity.RouteEntity;

@Repository
public interface RouteRequestRepository
        extends ReactiveCrudRepository<RouteEntity, Long> {

    Mono<RouteEntity> findByTag(String tag);

}
