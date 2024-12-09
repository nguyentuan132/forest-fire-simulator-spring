package com.ciril.forestfiresimulation.config;

import java.util.Arrays;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "simulation")
@Data
public class FireSimulationConfig {

    private Integer height;
    private Integer width;
    private String initialFirePositions;
    private Double fireSpreadProbability;
    private Integer[][] parsedInitialFirePositions;

    @PostConstruct
    public void parseFirePositions() {
        // Parse la chaîne en un tableau bidimensionnel
        parsedInitialFirePositions = Arrays.stream(initialFirePositions.split(";"))
                .map(row -> Arrays.stream(row.split(","))
                        .map(Integer::parseInt)
                        .toArray(Integer[]::new))
                .toArray(Integer[][]::new);
    }
}
