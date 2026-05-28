package com.project.hamburger_weather.infra.persistence;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import com.project.hamburger_weather.domain.model.Coordinate;
import com.project.hamburger_weather.domain.model.SavedRoute;
import com.project.hamburger_weather.infra.persistence.entity.RouteEntity;
import com.project.hamburger_weather.infra.persistence.mapper.SavedRouteMapper;
import static org.assertj.core.api.Assertions.assertThat;
import com.project.hamburger_weather.domain.model.Address;

@ExtendWith(MockitoExtension.class)
public class SavedRouteMapperTest {

    private final SavedRouteMapper mapper = new SavedRouteMapper();

    @Test
    void shouldMapEntityToDomainCorrectly() {
        RouteEntity entity = RouteEntity.builder()
                .tag("home-to-work")
                .startStreet("Musterstraße")
                .startHouseNumber("1")
                .startPlz("20095")
                .startCity("Hamburg")
                .startCountry("Germany")
                .endStreet("Reeperbahn")
                .endHouseNumber("1")
                .endPlz("20359")
                .endCity("Hamburg")
                .endCountry("Germany")
                .coordinates("[{\"latitude\":53.55,\"longitude\":10.0}]")
                .requestedAt(LocalDateTime.now())
                .build();

        SavedRoute domain = mapper.toDomain(entity);

        assertThat(domain.tag()).isEqualTo("home-to-work");
        assertThat(domain.startAddress().city()).isEqualTo("Hamburg");
        assertThat(domain.endAddress().street()).isEqualTo("Reeperbahn");
        assertThat(domain.coordinates()).hasSize(1);
        assertThat(domain.coordinates().get(0).latitude()).isEqualTo(53.55);
    }

    @Test
    void shouldMapDomainToEntityCorrectly() {
        SavedRoute domain = buildTestDomain();

        RouteEntity entity = mapper.toEntity(
                "home-to-work",
                domain.startAddress(),
                domain.endAddress(),
                domain.coordinates()
        );

        assertThat(entity.getId()).isNull(); // must be null for db insert
        assertThat(entity.getTag()).isEqualTo("home-to-work");
        assertThat(entity.getCoordinates()).contains("latitude");
        assertThat(entity.getRequestedAt()).isNotNull();
    }

    @Test
    void shouldSerializeAndDeserializeCoordinatesCorrectly() {
        List<Coordinate> original = List.of(
                new Coordinate(53.55, 10.0),
                new Coordinate(53.56, 10.1)
        );

        SavedRoute domain = new SavedRoute(
                "test",
                buildTestAddress(),
                buildTestAddress(),
                original,
                LocalDateTime.now()
        );

        RouteEntity entity = mapper.toEntity("test",
                domain.startAddress(), domain.endAddress(), domain.coordinates());
        SavedRoute restored = mapper.toDomain(entity);

        assertThat(restored.coordinates()).hasSize(2);
        assertThat(restored.coordinates().get(0).latitude()).isEqualTo(10.0);
    }

    private SavedRoute buildTestDomain() {
        return new SavedRoute(
                "home-to-work",
                buildTestAddress(),
                buildTestAddress(),
                List.of(new Coordinate(53.55, 10.0)),
                LocalDateTime.now()
        );
    }

    private Address buildTestAddress() {
        return new Address(
                "Musterstraße",
                "1",
                "20095",
                "Hamburg",
                "Germany"
        );
    }
}
