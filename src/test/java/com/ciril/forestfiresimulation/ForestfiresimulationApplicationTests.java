package com.ciril.forestfiresimulation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
import com.ciril.forestfiresimulation.model.Cell;
import com.ciril.forestfiresimulation.model.Forest;
import com.ciril.forestfiresimulation.service.FireSimulationService;

@SpringBootTest
class ForestfiresimulationApplicationTests {

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
	public void testRunSimulation_InitialState() {
		// Run the simulation
		List<Forest> simulationSteps = fireSimulationService.runSimulation();

		// Check that there is at least one step (initial step with fire)
		assertTrue(simulationSteps.size() > 0, "Simulation should include at least one step");

		// Check the initial state of the first simulation step (fire should be present)
		Forest initialForest = simulationSteps.get(0);
		assertTrue(initialForest.getGrid()[0][1].getState() == Cell.State.FIRE,
				"Fire should be present at initial position (0,1)");
		assertTrue(initialForest.getGrid()[1][1].getState() == Cell.State.FIRE,
				"Fire should be present at initial position (1,1)");
	}

	@Test
	public void testRunSimulation_FireSpread() {
		// Run the simulation for several steps
		List<Forest> simulationSteps = fireSimulationService.runSimulation();

		// Ensure there are at least two steps (fire should have spread)
		assertTrue(simulationSteps.size() > 1, "There should be more than one step in the simulation");

		// Check that fire has spread to other cells (other than initial positions)
		Forest secondStep = simulationSteps.get(1);
		boolean fireSpread = false;
		for (int i = 0; i < secondStep.getHeight(); i++) {
			for (int j = 0; j < secondStep.getWidth(); j++) {
				if (secondStep.getGrid()[i][j].getState() == Cell.State.FIRE) {
					fireSpread = true;
					break;
				}
			}
		}

		assertTrue(fireSpread, "Fire should have spread to at least one adjacent cell");
	}

	@Test
	public void testInitializeForest() {
		// Initialize the forest
		Forest forest = fireSimulationService.initializeForest();

		// Check that the forest has the correct size
		assertEquals(3, forest.getHeight(), "Forest height should be 3");
		assertEquals(3, forest.getWidth(), "Forest width should be 3");

		// Verify that the initial fire positions are correctly set
		assertTrue(forest.getGrid()[0][1].getState() == Cell.State.FIRE, "Fire should be at position (0,1)");
		assertTrue(forest.getGrid()[1][1].getState() == Cell.State.FIRE, "Fire should be at position (1,1)");
	}

	@Test
	public void testIsFirePresent() {
		// Create an initial forest with fire
		Forest forest = fireSimulationService.initializeForest();

		// Check that fire is present
		assertTrue(fireSimulationService.isFirePresent(forest), "Fire should be present in the forest");

		// Change the forest state to no fire
		forest.getGrid()[0][1].setState(Cell.State.ASH);
		forest.getGrid()[1][1].setState(Cell.State.ASH);

		assertFalse(fireSimulationService.isFirePresent(forest),
				"Fire should no longer be present after being extinguished");
	}

}
