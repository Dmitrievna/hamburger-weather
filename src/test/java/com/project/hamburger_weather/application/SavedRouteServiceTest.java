package com.project.hamburger_weather.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.project.hamburger_weather.infra.persistence.entity.RouteEntity;
import com.project.hamburger_weather.infra.persistence.mapper.SavedRouteMapper;
import com.project.hamburger_weather.infra.persistence.repository.RouteRequestRepository;
import com.project.hamburger_weather.infra.support.CoordinatesOptimizator;
import reactor.core.publisher.Mono;
import com.project.hamburger_weather.domain.model.SavedRoute;
import com.project.hamburger_weather.infra.api.GeoConverterService;
import com.project.hamburger_weather.infra.api.RoutingService;

@ExtendWith(MockitoExtension.class)
public class SavedRouteServiceTest {

    @Mock
    private RouteRequestRepository repository;

    @Mock
    private RoutingService routingService;

    @Mock
    private GeoConverterService geoConverterService;

    @Mock
    private CoordinatesOptimizator coordinatesOptimizator;

    @Mock
    private SavedRouteMapper mapper;

    @InjectMocks
    private SavedRouteService savedRouteService;

    // to test
    // road exists in db
    // road doesnt exist in db
    // some kind of error? like 
    // server not responding from 
    // either coordinates service or 
    // routing service
}
