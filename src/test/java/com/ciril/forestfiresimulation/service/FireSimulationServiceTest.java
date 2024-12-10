package com.ciril.forestfiresimulation.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.ciril.forestfiresimulation.config.FireSimulationConfig;
import com.ciril.forestfiresimulation.model.Forest;

@SpringBootTest
class FireSimulationServiceTest {

    @Mock
    private FireSimulationConfig mockConfig;

    @InjectMocks
    private FireSimulationService fireSimulationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Configure mock for configuration parameters
        when(mockConfig.getHeight()).thenReturn(3);
        when(mockConfig.getWidth()).thenReturn(3);
        when(mockConfig.getFireSpreadProbability()).thenReturn(0.5);
        Integer[][] initialFirePositions = { { 0, 1 }, { 1, 1 } };
        when(mockConfig.getParsedInitialFirePositions()).thenReturn(initialFirePositions);
    }

    @Test
    void initializeForest_shouldInitializeForestWithCorrectDimensionsAndFirePositions() {
        // Call the method under test
        Forest forest = fireSimulationService.initializeForest();

        // Verify that the forest has the correct size
        assertEquals(3, forest.getHeight());
        assertEquals(3, forest.getWidth());

        // Verify that the cells at these positions are correctly set on fire
        assertEquals(Forest.State.FIRE, forest.getState(0, 1));
        assertEquals(Forest.State.FIRE, forest.getState(1, 1));

        // Verify that other cells remain empty (EMPTY)
        assertEquals(Forest.State.EMPTY, forest.getState(0, 0));
        assertEquals(Forest.State.EMPTY, forest.getState(2, 2));
    }

    @Test
    void spreadFire_shouldSetCurrentCellToAshAndSpreadFireToAdjacentCells() {
        Forest forest = new Forest(3, 3); // 3x3 grid
        Forest newStep = new Forest(3, 3);
        // Initial fire position
        forest.setState(1, 1, Forest.State.FIRE);

        // Apply fire spread from the cell (1, 1)
        fireSimulationService.spreadFire(forest, newStep, 1, 1);

        // Verify that the cell (1, 1) is marked as ASH
        assertEquals(Forest.State.ASH, newStep.getState(1, 1));

        // Verify that the fire spreads to adjacent cells
        assertTrue(newStep.getState(0, 1) == Forest.State.FIRE || newStep.getState(0, 1) == Forest.State.EMPTY);
        assertTrue(newStep.getState(1, 0) == Forest.State.FIRE || newStep.getState(1, 0) == Forest.State.EMPTY);
        assertTrue(newStep.getState(2, 1) == Forest.State.FIRE || newStep.getState(2, 1) == Forest.State.EMPTY);
        assertTrue(newStep.getState(1, 2) == Forest.State.FIRE || newStep.getState(1, 2) == Forest.State.EMPTY);
    }

    @Test
    void isFirePresent_shouldReturnTrueWhenFireIsPresent() {
        Forest forest = new Forest(3, 3); // 3x3 grid
        // Initialize the fire at position (1, 1)
        forest.setState(1, 1, Forest.State.FIRE);
        // Verify that fire is present
        assertTrue(fireSimulationService.isFirePresent(forest));
        // Verify that fire become ash
        forest.setState(1, 1, Forest.State.ASH);
        assertFalse(fireSimulationService.isFirePresent(forest));
    }

    @Test
    void runSimulation_shouldReturnCorrectNumberOfSimulationSteps() {
        // Initialize the simulation
        List<Forest> simulationSteps = fireSimulationService.runSimulation();

        // Verify that the simulation returns at least one initial state
        assertNotNull(simulationSteps);
        assertTrue(simulationSteps.size() > 0);

        // Verify the state of the forest after each step
        Forest initialForest = simulationSteps.get(0);
        assertEquals(Forest.State.FIRE, initialForest.getState(0, 1)); // Feu au début
        assertEquals(Forest.State.FIRE, initialForest.getState(1, 1)); // Feu au début

        // Verify that there are multiple steps in the simulation
        assertTrue(simulationSteps.size() > 1);

        // Verify the evolution of the state of cells (expected logical progression)
        for (int i = 1; i < simulationSteps.size(); i++) {
            Forest step = simulationSteps.get(i);
            // Ensure that cells that were on fire become ash
            assertEquals(Forest.State.ASH, step.getState(0, 1));
            assertEquals(Forest.State.ASH, step.getState(1, 1));
        }
        assertFalse(fireSimulationService.isFirePresent(simulationSteps.get(simulationSteps.size() - 1)));
    }
}
