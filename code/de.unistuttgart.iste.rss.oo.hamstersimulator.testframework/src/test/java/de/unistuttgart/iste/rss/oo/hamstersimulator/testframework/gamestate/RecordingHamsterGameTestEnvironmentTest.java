package de.unistuttgart.iste.rss.oo.hamstersimulator.testframework.gamestate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.ObservableHamster;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.LocationVector;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

/**
 * Test to test the functionality of RecordingHamsterGameTestEnvironment.
 * It creates a hamster game, executes it and records all game states. Later
 * it demonstrates how the game states can be used to track all movements, etc.
 * of the hamster on the territory.
 * @author Steffen Becker
 */
public final class RecordingHamsterGameTestEnvironmentTest {

    /**
     * Number of rows paule and paule walk towards the south in this scenario.
     */
    private static final int ROWS_WALKED = 5;

    /**
     * Expected location of paule after the game.
     */
    private static final Location FINAL_HAMSTER_LOCATION = Location.from(5, 0);

    /**
     * Used in the test to assert the total number of states the executed
     * game had.
     */
    private static final int EXPECTED_NO_OF_STATES = 23;

    /**
     * RecordingHamsterGameTestEnvironment for all tests.
     */
    private RecordingHamsterGameTestEnvironment testEnvironment;

    /**
     * Reference to the default hamster on the game territory.
     */
    private ObservableHamster paule;

    /**
     * Initializes the RecordingHamsterGameTestEnvironment with the game defined in {@link TestSimpleHamsterGame}.
     */
    @BeforeEach
    public void initTestEnvironment() {
        testEnvironment = new RecordingHamsterGameTestEnvironment(new TestSimpleHamsterGame());
        paule = testEnvironment.getViewModel().getTerritory().getDefaultHamster();
    }


    /**
     * This tests whether the recording of game states works while the hamster game is executing.
     */
    @Test
    public void testOnlineRecorder() {
        final ObservableList<GameState> gameStates = testEnvironment.getGameStates();
        final AtomicInteger additions = new AtomicInteger(0);
        gameStates.addListener((ListChangeListener<GameState>) change -> {
            while (change.next()) {
                additions.addAndGet(change.getAddedSize());
            }
        });
        testEnvironment.runGame();
        assertEquals(EXPECTED_NO_OF_STATES - 1, additions.get());
    }

    /**
     * Tests recording of the grain counts on the tiles of the territory.
     */
    @Test
    public void testTerritoryStateRecording() {
        testEnvironment.runGame();
        final List<GameState> gameStates = testEnvironment.getGameStates();
        assertEquals(EXPECTED_NO_OF_STATES, gameStates.size());
        final GameState initialGameState = gameStates.get(0);
        final GameState beforePaulaState = gameStates.get(12);
        final GameState finalGameState = gameStates.get(gameStates.size() - 1);
        for (int row = 0; row < ROWS_WALKED; row++) {
            final LocationVector vector = new LocationVector(row, 0);
            assertEquals(0, initialGameState.grainCountAt(Location.ORIGIN.translate(vector)));
            assertEquals(1, beforePaulaState.grainCountAt(Location.ORIGIN.translate(vector)));
            assertEquals(0, finalGameState.grainCountAt(Location.ORIGIN.translate(vector)));
        }
    }

    /**
     * Tests recording of written messages.
     */
    @Test
    public void testWrittenMessageRecording() {
        testEnvironment.runGame();
        final List<GameState> gameStates = testEnvironment.getGameStates();
        assertEquals(EXPECTED_NO_OF_STATES, gameStates.size());
        final GameState initialGameState = gameStates.get(0);
        final GameState beforePaulaState = gameStates.get(12);
        assertEquals(0, initialGameState.getMessageList().size());
        assertEquals(1, beforePaulaState.getMessageList().size());
        assertEquals("Done placing grains", beforePaulaState.getMessageList().get(0).getMessage());
        assertEquals(paule, beforePaulaState.getMessageList().get(0).getHamster());
    }

    /**
     * Tests if hamster states are recorded correctly.
     */
    @Test
    public void testHamsterStateRecording() {
        testEnvironment.runGame();
        final List<GameState> gameStates = testEnvironment.getGameStates();
        final ObservableHamster paula = testEnvironment.getViewModel().getTerritory().getHamsters().get(1);
        final List<HamsterState> paulesStates = gameStates.stream().map(gameState -> gameState.getHamsterState(paule))
                .collect(Collectors.toList());
        assertEquals(EXPECTED_NO_OF_STATES, gameStates.size());
        final GameState finalGameState = gameStates.get(gameStates.size() - 1);
        assertEquals(Location.ORIGIN, paulesStates.get(0).getLocation());
        assertEquals(FINAL_HAMSTER_LOCATION, paulesStates.get(EXPECTED_NO_OF_STATES - 1).getLocation());
        assertEquals(FINAL_HAMSTER_LOCATION, finalGameState.getHamsterState(paula).getLocation());
        assertEquals(ROWS_WALKED, finalGameState.getHamsterState(paule).getGrainDropped());
        assertEquals(ROWS_WALKED, finalGameState.getHamsterState(paula).getGrainCollected());
    }

    /**
     * Tests if game states have proper time stamps and are connected correctly.
     */
    @Test
    public void testHamsterStateLinking() {
        testEnvironment.runGame();
        final List<GameState> gameStates = testEnvironment.getGameStates();
        assertEquals(EXPECTED_NO_OF_STATES, gameStates.size());
        final GameState initialGameState = gameStates.get(0);
        final GameState finalGameState = gameStates.get(gameStates.size() - 1);
        for (int i = 0; i < gameStates.size(); i++) {
            final GameState ithGameState = gameStates.get(i);
            assertEquals(i, ithGameState.getTimestamp());
            assertTrue(!(i == 0) || ithGameState.isInitialState());
            assertTrue(!(i != 0) || !ithGameState.isInitialState());
            assertTrue(!(i == gameStates.size() - 1) || ithGameState.isFinalState());
            assertTrue(!(i != gameStates.size() - 1) || !ithGameState.isFinalState());
        }
        GameState current = finalGameState;
        while (!current.isInitialState()) {
            current = current.getPreviousGameState();
        }
        assertEquals(initialGameState, current);
        while (!current.isFinalState()) {
            current = current.getNextGameState();
        }
        assertEquals(finalGameState, current);
    }
}
