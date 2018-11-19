package de.unistuttgart.iste.rss.oo.hamstersimulator.main.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import de.unistuttgart.iste.rss.oo.hamstersimulator.exceptions.FrontBlockedException;
import de.unistuttgart.iste.rss.oo.hamstersimulator.external.model.Hamster;
import de.unistuttgart.iste.rss.oo.hamstersimulator.external.model.HamsterGame;

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
        game.initialize("/territories/example02.ter");
        paule = game.getTerritory().getDefaultHamster();
    }

    /**
     * Test which tests exception when running against a wall.
     */
    @Test
    public void testFailedMove() {
        game.startGame(false);
        assertFalse(paule.frontIsClear());
        assertThrows(FrontBlockedException.class, () -> {
            paule.move();
        });
        game.stopGame();
    }

    /**
     * Test which tests exception when running against a wall.
     */
    @Test
    public void testFailedMove2() {
        game.startGame(false);
        paule.turnLeft();
        assertFalse(paule.frontIsClear());
        assertThrows(FrontBlockedException.class, () -> {
            paule.move();
        });
        game.stopGame();
    }

}
