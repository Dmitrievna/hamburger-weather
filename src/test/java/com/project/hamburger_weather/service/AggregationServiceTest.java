 package com.project.hamburger_weather.service;

// import static org.mockito.ArgumentMatchers.*;
// import static org.mockito.Mockito.*;
// import java.util.List;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;
// import com.project.hamburger_weather.dto.AddressDto;
// import com.project.hamburger_weather.dto.CoordinatesDto;
// import com.project.hamburger_weather.dto.ReportDto;
// import com.project.hamburger_weather.dto.RouteDto;
// import com.project.hamburger_weather.dto.WeatherForecastDto;
// import reactor.core.publisher.Flux;
// import reactor.core.publisher.Mono;
// import reactor.test.StepVerifier;





// // CAUTION

class AggregationServiceTest {}


// @ExtendWith(MockitoExtension.class)
// class AggregationServiceTest {

//     @Mock
//     private WeatherService weatherService;

//     @Mock
//     private RoutingService routingService;

//     @Mock
//     private GeoConverterService geoConverterService;

//     @Mock
//     private WeatherSummaryService weatherSummaryService;

//     @Mock
//     private CoordinatesOptimizationService coordinatesOptimizationService;

//     private AggregationService aggregationService;

//     @BeforeEach
//     void setUp() {
//         aggregationService = new AggregationService(weatherService, routingService,
//                 geoConverterService, weatherSummaryService, coordinatesOptimizationService);
//     }

//     @Test
//     void testGetTheAnswer() {
//         AddressDto startAddress = new AddressDto("Main St", "1", "10001", "Hamburg", "Germany");
//         AddressDto endAddress = new AddressDto("Second St", "2", "10002", "Hamburg", "Germany");

//         CoordinatesDto startCoords = new CoordinatesDto("53.5511", "10.0046");
//         CoordinatesDto endCoords = new CoordinatesDto("53.5621", "9.9946");
//         List<CoordinatesDto> routeCoordinates = List.of(startCoords, endCoords);

//         RouteDto route = new RouteDto(routeCoordinates);
//         RouteDto optimizedRoute = new RouteDto(routeCoordinates);
//         WeatherForecastDto forecast = new WeatherForecastDto();
//         ReportDto report = new ReportDto();


//         // mock responses to not depend on third services
//         when(geoConverterService.getCoordinates(anyString(), anyString(), anyString(), anyString(), anyString()))
//                 .thenReturn(Mono.just(startCoords), Mono.just(endCoords));
//         when(routingService.getRoute(anyDouble(), anyDouble(), anyDouble(), anyDouble()))
//                 .thenReturn(Mono.just(route));
//         when(coordinatesOptimizationService.deduplicate(anyList(), anyDouble()))
//                 .thenReturn(optimizedRoute);
//         when(weatherService.getForecast(anyDouble(), anyDouble()))
//                 .thenReturn(Mono.just(new Object()));
//         when(weatherSummaryService.summarizeToReport(any(Flux.class)))
//                 .thenReturn(Mono.just(report));

//         StepVerifier.create(aggregationService.getTheAnswer(startAddress, endAddress))
//                 .expectNext(report)
//                 .verifyComplete();
//     }

//     @Test
//     void testBuildRoute() {
//         AddressDto startAddress = new AddressDto("Main St", "1", "10001", "Hamburg", "Germany");
//         AddressDto endAddress = new AddressDto("Second St", "2", "10002", "Hamburg", "Germany");

//         CoordinatesDto startCoords = new CoordinatesDto(53.5511, 10.0046);
//         CoordinatesDto endCoords = new CoordinatesDto(53.5621, 9.9946);
//         RouteDto route = new RouteDto(List.of(startCoords, endCoords));

//         when(geoConverterService.getCoordinates(anyString(), anyString(), anyString(), anyString(), anyString()))
//                 .thenReturn(Mono.just(startCoords), Mono.just(endCoords));
//         when(routingService.getRoute(anyDouble(), anyDouble(), anyDouble(), anyDouble()))
//                 .thenReturn(Mono.just(route));

//         StepVerifier.create(aggregationService.buildRoute(startAddress, endAddress))
//                 .expectNext(route)
//                 .verifyComplete();
//     }

//     @Test
//     void testGetAllForecasts() {
//         CoordinatesDto coord1 = new CoordinatesDto(53.5511, 10.0046);
//         CoordinatesDto coord2 = new CoordinatesDto(53.5621, 9.9946);
//         RouteDto route = new RouteDto(List.of(coord1, coord2));

//         WeatherForecastDto forecast = new WeatherForecastDto();

//         when(weatherService.getForecast(anyDouble(), anyDouble()))
//                 .thenReturn(Mono.just(new Object()));

//         StepVerifier.create(aggregationService.getAllForecasts(Mono.just(route)))
//                 .expectNextCount(2)
//                 .verifyComplete();
//     }
// }