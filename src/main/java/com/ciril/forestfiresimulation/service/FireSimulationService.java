package com.ciril.forestfiresimulation.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciril.forestfiresimulation.config.FireSimulationConfig;
import com.ciril.forestfiresimulation.model.Cell;
import com.ciril.forestfiresimulation.model.Forest;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FireSimulationService {

    @Autowired
    private FireSimulationConfig config;

    private Random random = new Random();

    public List<Forest> runSimulation() {
        log.info("config : " + config);
        Forest forest = new Forest(config.getHeight(), config.getWidth());

        // Initialize fire positions
        for (Integer[] position : config.getParsedInitialFirePositions()) {
            forest.setFire(position[0], position[1]);
        }

        List<Forest> simulationSteps = new ArrayList<>();
        simulationSteps.add(forest.clone()); // Save the initial state

        boolean fireExists = this.checkIfFireExists(forest);
        while (fireExists) {
            // Clone the forest once per step
            Forest newStep = forest.clone();

            // Iterate over the grid to spread fire
            for (int i = 0; i < forest.getHeight(); i++) {
                for (int j = 0; j < forest.getWidth(); j++) {
                    if (forest.getGrid()[i][j].getState() == Cell.State.FIRE) {
                        spreadFire(forest, newStep, i, j);
                    }
                }
            }

            // Add the new step and update the forest
            simulationSteps.add(newStep);
            forest = newStep;
            fireExists = this.checkIfFireExists(forest);
        }

        return simulationSteps; // Return the list of steps directly
    }

    private void spreadFire(Forest forest, Forest newStep, int x, int y) {
        int[][] directions = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };

        // Set current cell to ash
        newStep.getGrid()[x][y].setState(Cell.State.ASH);

        // Try to spread fire to adjacent cells
        for (int[] dir : directions) {
            int newX = x + dir[0];
            int newY = y + dir[1];
            if (newX >= 0 && newX < forest.getHeight() && newY >= 0 && newY < forest.getWidth()) {
                if (forest.getGrid()[newX][newY].getState() == Cell.State.EMPTY) {
                    if (random.nextDouble() < config.getFireSpreadProbability()) {
                        newStep.setFire(newX, newY);
                    }
                }
            }
        }
    }

    public boolean checkIfFireExists(Forest forest) {
        // Iterate through the grid and return true as soon as we find a fire
        for (int i = 0; i < forest.getHeight(); i++) {
            for (int j = 0; j < forest.getWidth(); j++) {
                if (forest.getGrid()[i][j].getState() == Cell.State.FIRE) {
                    return true;
                }
            }
        }
        return false;
    }
}
