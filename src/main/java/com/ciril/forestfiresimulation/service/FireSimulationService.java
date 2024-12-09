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

        Forest forest = initializeForest();

        List<Forest> simulationSteps = new ArrayList<>();
        simulationSteps.add(forest.clone()); // Save the initial state

        while (isFirePresent(forest)) {
            forest = simulateNextStep(forest);
            simulationSteps.add(forest);
        }

        return simulationSteps; // Return the list of steps directly
    }

    public Forest initializeForest() {
        Forest forest = new Forest(config.getHeight(), config.getWidth());
        initializeFirePositions(forest);
        return forest;
    }

    private void initializeFirePositions(Forest forest) {
        for (Integer[] position : config.getParsedInitialFirePositions()) {
            forest.setFire(position[0], position[1]);
        }
    }

    private Forest simulateNextStep(Forest forest) {
        Forest newStep = forest.clone();
        spreadFireAcrossGrid(forest, newStep);
        return newStep;
    }

    private void spreadFireAcrossGrid(Forest forest, Forest newStep) {
        for (int i = 0; i < forest.getHeight(); i++) {
            for (int j = 0; j < forest.getWidth(); j++) {
                if (forest.getGrid()[i][j].getState() == Cell.State.FIRE) {
                    spreadFire(forest, newStep, i, j);
                }
            }
        }
    }

    public void spreadFire(Forest forest, Forest newStep, int x, int y) {
        int[][] directions = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };

        // Set current cell to ash
        newStep.getGrid()[x][y].setState(Cell.State.ASH);

        // Try to spread fire to adjacent cells
        for (int[] dir : directions) {
            spreadFireToAdjacentCell(forest, newStep, x, y, dir);
        }
    }

    private void spreadFireToAdjacentCell(Forest forest, Forest newStep, int x, int y, int[] dir) {
        int newX = x + dir[0];
        int newY = y + dir[1];
        if (isCellValid(newX, newY, forest) && isCellEmpty(forest, newX, newY)) {
            if (random.nextDouble() < config.getFireSpreadProbability()) {
                newStep.setFire(newX, newY);
            }
        }
    }

    private boolean isCellValid(int x, int y, Forest forest) {
        return x >= 0 && x < forest.getHeight() && y >= 0 && y < forest.getWidth();
    }

    private boolean isCellEmpty(Forest forest, int x, int y) {
        return forest.getGrid()[x][y].getState() == Cell.State.EMPTY;
    }

    public boolean isFirePresent(Forest forest) {
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
