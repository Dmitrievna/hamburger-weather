package com.project.hamburger_weather.infra.loader;

import java.util.List;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;
import com.project.hamburger_weather.domain.model.Coordinate;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import com.project.hamburger_weather.domain.model.AccidentStats;
import org.springframework.core.io.Resource;
import java.util.Arrays;
import com.project.hamburger_weather.domain.model.RoadCondition;
import com.project.hamburger_weather.domain.model.LightCondition;

@Component
public class AccidentDataLoader {

    private List<AccidentStats> accidentStats = new ArrayList<>();
    List<String> streetConditionNames = List.of("IstStrassenzustand", "STRZUSTAND");

    @PostConstruct
    public void loadAccidentData() {
        try {
            PathMatchingResourcePatternResolver resolver
                    = new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources(
                    "classpath:data/**/*.csv"
            );

            accidentStats = Arrays.stream(resources)
                    .flatMap(resource -> parseFile(resource).stream())
                    .toList();

        } catch (IOException e) {
            throw new RuntimeException("Failed to load accident data", e);
        }
    }

    private List<AccidentStats> parseFile(Resource resource) {

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {

            System.out.println("Loading: " + resource.getFilename());

            List<String> columns = parseFirstLine(reader.readLine());

            return reader.lines()
                    .skip(1)
                    .map(line -> parseLine(line, columns))
                    .filter(stats -> stats != null)
                    .toList();

        } catch (IOException e) {
            System.err.println("Failed to load file: " + resource.getFilename());
            return List.of();
        }
    }

    private AccidentStats parseLine(String line, List<String> columns) {
        try {
            String[] parts = line.split(";");
            Integer isBikeAccident = Integer.valueOf(parts[columns.indexOf("IstRad")]);
            if (isBikeAccident == 1) {
                double lat = Double.parseDouble(parts[columns.indexOf("XGCSWGS84")].trim().replace(",", "."));
                double lon = Double.parseDouble(parts[columns.indexOf("YGCSWGS84")].trim().replace(",", "."));
                RoadCondition roadCondition = mapRoadCondition(getValueDifferentColumnNames(parts, columns, streetConditionNames));

                LightCondition lightCondition = mapLightCondition(parts[columns.indexOf("ULICHTVERH")]);
                return new AccidentStats(new Coordinate(lat, lon), roadCondition, lightCondition);
            }
            return null;

        } catch (Exception e) {
            throw new RuntimeException("Error parsing line: " + line, e);
        }
    }

    private List<String> parseFirstLine(String firstLine) {

        try {
            String[] parts = firstLine.split(";");

            return List.of(parts);
        } catch (Exception e) {
            throw new RuntimeException("Error parsing first line: " + firstLine, e);
        }

    }

    private String getValueDifferentColumnNames(String[] parts, List<String> columns, List<String> namesToTry) {
        for (String name : namesToTry) {
            if (columns.contains(name)) {
                return parts[columns.indexOf(name)];
            }
        }
        return null;
    }

    private RoadCondition mapRoadCondition(String value) {
        return switch (value) {
            case "1" ->
                RoadCondition.RAIN;
            case "2" ->
                RoadCondition.SNOW;
            default ->
                RoadCondition.DRY;
        };
    }

    private LightCondition mapLightCondition(String value) {
        return switch (value) {
            case "1" ->
                LightCondition.TWILIGHT;
            case "2" ->
                LightCondition.NIGHT;
            default ->
                LightCondition.CLEAR;
        };
    }

    public List<AccidentStats> getAccidentStats() {
        return accidentStats;
    }
}
