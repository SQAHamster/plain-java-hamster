package de.unistuttgart.iste.rss.oo.hamstersimulator.main.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.DummyInputInterface;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.LocationVector;
import de.unistuttgart.iste.rss.oo.hamstersimulator.external.model.Hamster;
import de.unistuttgart.iste.rss.oo.hamstersimulator.external.model.HamsterGame;

/**
 * Simple tests for the hamster API.
 * @author Steffen Becker
 *
 */
@TestInstance(Lifecycle.PER_CLASS)
public class BasicHamsterTests {

    private static final String territory = "5\n" + "3\n" + "#####\n" + "#> *#\n" + "#####\n" + "0\n" + "2\n" + "0\n"
            + "";

    private static final int TIMEOUT = 1000;

    /**
     * Delay used when running these hamster tests.
     */
    private static final double HAMSTER_GAME_TEST_SPEED = 0.1d;

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
     * @throws IOException 
     */
    @BeforeEach
    public void initializeTest() throws IOException {
        game.initialize(new ByteArrayInputStream(territory.getBytes()));
        game.getAdapter().addInputInterface(new DummyInputInterface());
        game.setSpeed(HAMSTER_GAME_TEST_SPEED);
        game.startGame(false);
        paule = game.getTerritory().getDefaultHamster();
    }

    /**
     * Test which tests a single default hamster move.
     */
    @Test
    public void testMove() {
        game.runGame(territory -> {
            final Location beforeLocation = paule.getLocation();
            for (int i = 0; i < 2; i++) {
                paule.move();
            }
            assertEquals(paule.getLocation(), Location.from(1, 3));
            assertEquals(paule.getLocation(), beforeLocation.translate(new LocationVector(0, 2)));
        });
    }

}
