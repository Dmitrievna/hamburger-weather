package com.project.hamburger_weather.persistence.repository;

import com.project.hamburger_weather.persistence.entity.RouteRequestEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface RouteRequestRepository
        extends ReactiveCrudRepository<RouteRequestEntity, Long> {

    Flux<RouteRequestEntity> findByTag(String tag);
}
