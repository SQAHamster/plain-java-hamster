package de.hamstersimulator.objectsfirst.main.tests;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.Mockito;

import de.hamstersimulator.objectsfirst.adapter.InputInterface;
import de.hamstersimulator.objectsfirst.exceptions.FrontBlockedException;
import de.hamstersimulator.objectsfirst.external.model.Hamster;
import de.hamstersimulator.objectsfirst.external.model.HamsterGame;

/**
 * Simple tests for the hamster API.
 * @author Steffen Becker
 *
 */
@TestInstance(Lifecycle.PER_CLASS)
public class HamsterBoundsTest {

    /**
     * Field containing the hamster game used in tests.
     */
    private HamsterGame game;

    /**
     * During a test, this contains a reference to the default hamster.
     */
    private Hamster paule;

    /**
     * Initialize a game and its UI.
     */
    @BeforeAll
    public void createHamsterGame() {
        game = new HamsterGame();
    }

    /**
     * Before each test, load the default territory.
     */
    @BeforeEach
    public void initializeTest() {
        game.initialize(getClass().getResourceAsStream("/_territories/example02.ter"));
        game.getModelViewAdapter().addInputInterface(getInputInterfaceMock());
        game.startGame();
        paule = game.getTerritory().getDefaultHamster();
    }

    /**
     * Test which tests exception when running against a wall.
     */
    @Test
    public void testFailedMove() {
        assertThrows(FrontBlockedException.class, () -> {
            game.runGame(territory -> {
                assertFalse(paule.frontIsClear());
                paule.move();
            });
        });
    }

    /**
     * Test which tests exception when running off the territory.
     */
    @Test
    public void testFailedMove2() {
        assertThrows(FrontBlockedException.class, () -> {
            game.runGame(territory -> {
                paule.turnLeft();
                assertFalse(paule.frontIsClear());
                paule.move();
            });
        });
    }

    private InputInterface getInputInterfaceMock() {
        InputInterface inputInterface = Mockito.mock(InputInterface.class);
        return inputInterface;
    }
}
