package de.hamstersimulator.objectsfirst.main.tests;

import static java.time.Duration.ofMillis;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.Mockito;

import de.hamstersimulator.objectsfirst.adapter.InputInterface;
import de.hamstersimulator.objectsfirst.datatypes.Direction;
import de.hamstersimulator.objectsfirst.datatypes.Location;
import de.hamstersimulator.objectsfirst.datatypes.LocationVector;
import de.hamstersimulator.objectsfirst.datatypes.Mode;
import de.hamstersimulator.objectsfirst.exceptions.FrontBlockedException;
import de.hamstersimulator.objectsfirst.exceptions.MouthEmptyException;
import de.hamstersimulator.objectsfirst.exceptions.NoGrainOnTileException;
import de.hamstersimulator.objectsfirst.external.model.Hamster;
import de.hamstersimulator.objectsfirst.external.model.HamsterGame;

/**
 * Simple tests for the hamster API.
 * @author Steffen Becker
 *
 */
@TestInstance(Lifecycle.PER_CLASS)
public class SimpleHamsterTests {

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
     */
    @BeforeEach
    public void initializeTest() {
        game.initialize();
        game.getModelViewAdapter().addInputInterface(getInputInterfaceMock());
        game.setSpeed(HAMSTER_GAME_TEST_SPEED);
        game.startGame();
        paule = game.getTerritory().getDefaultHamster();
    }

    /**
     * Test which tests a single default hamster move.
     */
    @Test
    public void testMove() {
        game.runGame(territory -> {
            final Location beforeLocation = paule.getLocation();
            paule.move();
            assertEquals(paule.getLocation(), Location.from(1, 2));
            assertEquals(paule.getLocation(), beforeLocation.translate(new LocationVector(0, 1)));
        });
    }

    /**
     * Test for the hamster's turn command.
     */
    @Test
    public void testTurn() {
        game.runGame(territory -> {
            assertEquals(paule.getDirection(), Direction.EAST);
            paule.turnLeft();
            assertEquals(paule.getDirection(), Direction.NORTH);
            paule.turnLeft();
            assertEquals(paule.getDirection(), Direction.WEST);
            paule.turnLeft();
            assertEquals(paule.getDirection(), Direction.SOUTH);
            paule.turnLeft();
            assertEquals(paule.getDirection(), Direction.EAST);
        });
    }

    /**
     * Test which tests exception when running against a wall.
     */
    @Test
    public void testFailedMove() {
        game.runGame(territory -> {
            assertThrows(FrontBlockedException.class, () -> {
                paule.turnLeft();
                paule.move();
            });
        });
    }

    /**
     * Test picking up a non-existing grain.
     */
    @Test
    public void testFailedPickup() {
        game.runGame(territory -> {
            assertThrows(NoGrainOnTileException.class, () -> {
                paule.pickGrain();
            });
        });
    }

    /**
     * Test putting a non-existing grain.
     */
    @Test
    public void testFailedPut() {
        game.runGame(territory -> {
            assertThrows(MouthEmptyException.class, () -> {
                paule.putGrain();
            });
        });
    }

    /**
     * Test for spawning a new hamster and registering it at the
     * territory.
     */
    @Test
    public void testSpawnHamster() {
        game.runGame(territory -> {
            final Location spawnLocation = Location.from(1, 2);
            assertEquals(game.getTerritory().getHamsters().size(), 1);
            assertEquals(paule, game.getTerritory().getHamsters().get(0));
            final Hamster marry = new Hamster(game.getTerritory(), spawnLocation, Direction.EAST, 0);
            assertEquals(game.getTerritory().getHamsters().size(), 2);
            marry.move();
            assertEquals(spawnLocation.translate(new LocationVector(0, 1)), marry.getLocation());
            assertEquals(marry, game.getTerritory().getHamsters().get(1));
            game.initialize();
            assertEquals(game.getTerritory().getHamsters().size(), 1);
            assertEquals(paule, game.getTerritory().getHamsters().get(0));
        });
    }

    /**
     * Test which tests catching a hamster game exception does still
     * stop to game and prevents further game commands.
     */
    @Test
    public void testExceptionMove() {
        game.runGame(territory -> {
            paule.move();
            paule.move();
            try {
                paule.move();
            } catch (FrontBlockedException fbe) {
                assertTrue(game.getCurrentGameMode() == Mode.STOPPED);
            }
        });
    }

    /**
     * Test which tests catching a hamster game exception does still
     * stop to game and prevents further game commands. It also ensures
     * that raising an exception inside a command does not stall the hamster
     * simulator in a half-paused mode.
     */
    @Test
    public void testExceptionMove2() {
        game.runGame(territory -> {
            paule.move();
            paule.move();
            try {
                paule.move();
            } catch (FrontBlockedException fbe) {
                assertThrows(IllegalStateException.class, () -> {
                    assertTimeoutPreemptively(ofMillis(TIMEOUT), () -> {
                        paule.turnLeft();
                    });
                });
            }
        });
    }

    private InputInterface getInputInterfaceMock() {
        final InputInterface inputInterface = Mockito.mock(InputInterface.class);
        return inputInterface;
    }
}
