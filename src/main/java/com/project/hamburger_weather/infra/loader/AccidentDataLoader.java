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
import java.io.Reader;
import com.project.hamburger_weather.domain.model.AccidentStats;
import org.springframework.core.io.Resource;
import java.util.Arrays;
import com.project.hamburger_weather.domain.model.RoadCondition;
import com.project.hamburger_weather.domain.model.LightCondition;
import java.util.Map;
import java.util.HashMap;

@Component
public class AccidentDataLoader {

    //private List<AccidentStats> accidentStats = new ArrayList<>();
    private Map<String, List<AccidentStats>> spatialIndex = new HashMap<>();

    List<String> streetConditionNames = List.of("IstStrassenzustand", "STRZUSTAND");

    //@PostConstruct
    //public void loadAccidentData() {
    //    try {
    //        PathMatchingResourcePatternResolver resolver
    //                = new PathMatchingResourcePatternResolver();
    //        Resource[] resources = resolver.getResources(
    //                "classpath:data/**/*.csv"
    //        );
    //
    //        accidentStats = Arrays.stream(resources)
    //                .flatMap(resource -> parseFile(resource).stream())
    //                .toList();
    //
    //    } catch (IOException e) {
    //        throw new RuntimeException("Failed to load accident data", e);
    //    }
    //} 
    @PostConstruct
    public void load() {

        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

        try {

            Resource[] resources = resolver.getResources("classpath:data/**/*.csv");

            Map<String, List<AccidentStats>> index = new HashMap<>();

            for (Resource res : resources) {

                System.out.println("Loading: " + res.getFilename());
                processFileIntoIndex(res, index);
                System.gc(); // force garbage collection

            }

            this.spatialIndex = index;
            // noch checken
            long total = index.values().stream().mapToLong(List::size).sum();
            System.out.println("Total loaded records into accident statistics: " + total);

        } catch (IOException e) {
            throw new RuntimeException("Failed to load accident data", e);
        }
    }

    // load only nearby accidents to memory when requested
    public List<AccidentStats> findNearby(Double lat, Double lon, Double radiusKm) {
        // calculate which cell to choose
        int cells = (int) Math.ceil(radiusKm / 1.0); // 1km cell size
        List<AccidentStats> nearby = new ArrayList<>();

        for (int dLat = -cells; dLat <= cells; dLat++) {
            for (int dLon = -cells; dLon <= cells; dLon++) {
                String key = Math.round(lat * 100 + dLat) + ":"
                        + Math.round(lon * 100 + dLon);
                List<AccidentStats> cell = spatialIndex.get(key);
                if (cell != null) {
                    nearby.addAll(cell);
                }
            }
        }

        return nearby;
    }

    private void processFileIntoIndex(Resource resource, Map<String, List<AccidentStats>> index) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            List<String> columns = parseFirstLine(reader.readLine());

            reader.lines()
                    .skip(1)
                    .forEach(line -> {
                        AccidentStats stats = parseLine(line, columns);
                        if (stats != null) {
                            String key = spatialKey(stats.coordinate().latitude(), stats.coordinate().longitude());
                            index.computeIfAbsent(key, k -> new ArrayList<>()).add(stats);
                        }
                    });

        } catch (IOException e) {
            System.err.println("Failed to load: " + resource.getFilename());
        }
    }

    /*private List<AccidentStats> parseFile(Resource resource) {

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
    } */
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

    private String spatialKey(double lat, double lon) {
        return Math.round(lat * 100) + ":" + Math.round(lon * 100);
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
}
