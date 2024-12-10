package com.ciril.forestfiresimulation.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciril.forestfiresimulation.config.FireSimulationConfig;
import com.ciril.forestfiresimulation.model.Forest;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FireSimulationService {

    @Autowired
    private FireSimulationConfig config;

    private Random random = new Random();

    // Runs the fire simulation and returns the list of forest states during the
    // simulation
    public List<Forest> runSimulation() {
        log.info("config : {}", config);

        // Initialize the forest with the configuration
        Forest forest = initializeForest();

        List<Forest> simulationSteps = new ArrayList<>();
        simulationSteps.add(new Forest(forest)); // Save the initial state

        // Continue the simulation while fire is still present in the forest
        while (isFirePresent(forest)) {
            forest = simulateNextStep(forest);
            simulationSteps.add(new Forest(forest)); // Add a copy of the new state
        }

        return simulationSteps;
    }

    // Initializes the forest based on the configuration
    public Forest initializeForest() {
        Forest forest = new Forest(config.getHeight(), config.getWidth());
        initializeFirePositions(forest);
        return forest;
    }

    // Sets the initial fire positions based on the configuration
    private void initializeFirePositions(Forest forest) {
        for (Integer[] position : config.getParsedInitialFirePositions()) {
            forest.setState(position[0], position[1], Forest.State.FIRE);
        }
    }

    // Simulates the next step of the fire spreading and returns the new state of
    // the forest
    private Forest simulateNextStep(Forest forest) {
        Forest newStep = new Forest(forest);
        for (int i = 0; i < forest.getHeight(); i++) {
            for (int j = 0; j < forest.getWidth(); j++) {
                if (forest.getState(i, j) == Forest.State.FIRE) {
                    spreadFire(forest, newStep, i, j);
                }
            }
        }
        return newStep;
    }

    // Spreads the fire from the current cell to adjacent cells
    public void spreadFire(Forest forest, Forest newStep, int x, int y) {
        int[][] directions = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };

        // Mark the current cell as ash
        newStep.setState(x, y, Forest.State.ASH);

        // Spread fire to adjacent cells
        for (int[] dir : directions) {
            spreadFireToAdjacentCell(forest, newStep, x, y, dir);
        }
    }

    // Attempts to spread fire to an adjacent cell if the conditions are met
    private void spreadFireToAdjacentCell(Forest forest, Forest newStep, int x, int y, int[] dir) {
        int newX = x + dir[0];
        int newY = y + dir[1];
        if (isCellValid(newX, newY, forest) && forest.getState(newX, newY) == Forest.State.EMPTY) {
            // Fire spreads based on the probability from the configuration
            if (random.nextDouble() < config.getFireSpreadProbability()) {
                newStep.setState(newX, newY, Forest.State.FIRE);
            }
        }
    }

    // Checks if a cell is valid within the bounds of the forest
    private boolean isCellValid(int x, int y, Forest forest) {
        return x >= 0 && x < forest.getHeight() && y >= 0 && y < forest.getWidth();
    }

    // Checks if fire is still present in the forest
    public boolean isFirePresent(Forest forest) {
        for (int i = 0; i < forest.getHeight(); i++) {
            for (int j = 0; j < forest.getWidth(); j++) {
                if (forest.getState(i, j) == Forest.State.FIRE) {
                    return true;
                }
            }
        }
        return false;
    }
}
