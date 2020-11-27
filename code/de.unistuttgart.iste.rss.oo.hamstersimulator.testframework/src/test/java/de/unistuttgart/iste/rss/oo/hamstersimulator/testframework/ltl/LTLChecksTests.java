package de.unistuttgart.iste.rss.oo.hamstersimulator.testframework.ltl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.function.Predicate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.ObservableHamster;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Direction;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;
import de.unistuttgart.iste.rss.oo.hamstersimulator.testframework.gamestate.GameState;
import de.unistuttgart.iste.rss.oo.hamstersimulator.testframework.gamestate.RecordingHamsterGameTestEnvironment;
import javafx.collections.ObservableList;

/**
 * Test to test the functionality of RecordingHamsterGameTestEnvironment.
 * It creates a hamster game, executes it and records all game states. Later
 * it demonstrates how the game states can be used to track all movements, etc.
 * of the hamster on the territory.
 * @author Steffen Becker
 */
public final class LTLChecksTests {

    /**
     * Number of rows paule and paule walk towards the south in this scenario.
     */
    private static final int ROWS_WALKED = 5;

    /**
     * Expected location of paule after the game.
     */
    private static final Location FINAL_HAMSTER_LOCATION = Location.from(5, 0);

    /**
     * RecordingHamsterGameTestEnvironment for all tests.
     */
    private RecordingHamsterGameTestEnvironment testEnvironment;

    /**
     * Reference to the default hamster on the game territory.
     */
    private ObservableHamster paule;

    final Predicate<GameState> pauleIsOnOriginCondition = state -> state.getHamsterState(paule).getLocation()
            .equals(Location.ORIGIN);
    final Predicate<GameState> pauleIsOnTargetCondition = state -> state.getHamsterState(paule).getLocation()
            .equals(FINAL_HAMSTER_LOCATION);
    final Predicate<GameState> pauleLooksToSouthCondition = state -> state.getHamsterState(paule)
            .getDirection() == Direction.SOUTH;
    final Predicate<GameState> stateWasReachedByDroppingAGrainCondition = state -> {
        if (state.isInitialState()) {
            return false;
        }
        final GameState previousGameState = state.getPreviousGameState();
        return (state.getHamsterState(paule).getGrainDropped() != previousGameState.getHamsterState(paule)
                .getGrainDropped());
    };
    final Predicate<GameState> stateWasReachedByMovingCondition = state -> {
        if (state.isInitialState()) {
            return false;
        }
        final GameState previousGameState = state.getPreviousGameState();
        return (!state.getHamsterState(paule).getLocation()
                .equals(previousGameState.getHamsterState(paule).getLocation()));
    };

    /**
     * Initializes the RecordingHamsterGameTestEnvironment with the game defined in {@link TestSimpleHamsterGame}.
     */
    @BeforeEach
    public void initTestEnvironment() {
        testEnvironment = new RecordingHamsterGameTestEnvironment(new TestSimpleHamsterGame());
        paule = testEnvironment.getViewModel().getTerritory().getDefaultHamster();
    }

    /**
     * Tests if game states have proper time stamps and are connected correctly.
     */
    @Test
    public void testBasicGameStatePredicate() {
        testEnvironment.runGame();
        final ObservableList<GameState> gameStates = testEnvironment.getGameStates();
        final BasicPredicate pauleIsOnOrigin = new BasicPredicate(gameStates, pauleIsOnOriginCondition);
        final Collection<GameState> matchingStates = pauleIsOnOrigin.getMatchingStates();
        assertEquals(2, matchingStates.size());
        for (final GameState state : gameStates) {
            assertTrue(!(state.getTimestamp() <= 1) || matchingStates.contains(state));
            assertTrue(!(state.getTimestamp() > 1) || !matchingStates.contains(state));
        }
    }

    /**
     * Tests if game states have proper time stamps and are connected correctly.
     */
    @Test
    public void testFinallyOperator() {
        testEnvironment.runGame();
        final ObservableList<GameState> gameStates = testEnvironment.getGameStates();
        final BasicPredicate pauleIsOnTarget = new BasicPredicate(gameStates, pauleIsOnTargetCondition);
        assertTrue(new FinallyFormula(pauleIsOnTarget).appliesTo(gameStates.get(0)));
        assertFalse(new NotFormula(new FinallyFormula(pauleIsOnTarget)).appliesTo(gameStates.get(0)));
    }

    /**
     * Tests if game states have proper time stamps and are connected correctly.
     */
    @Test
    public void testGloballyOperator() {
        testEnvironment.runGame();
        final ObservableList<GameState> gameStates = testEnvironment.getGameStates();
        final BasicPredicate pauleLooksToSouth = new BasicPredicate(gameStates, pauleLooksToSouthCondition);
        assertTrue(new GloballyFormula(pauleLooksToSouth).appliesTo(gameStates.get(0)));
        assertFalse(new NotFormula(new GloballyFormula(pauleLooksToSouth)).appliesTo(gameStates.get(0)));
    }

    /**
     * Tests if game states have proper time stamps and are connected correctly.
     */
    @Test
    public void testUntilOperator() {
        testEnvironment.runGame();
        final ObservableList<GameState> gameStates = testEnvironment.getGameStates();
        final BasicPredicate stateReachedByMoving = new BasicPredicate(gameStates, stateWasReachedByMovingCondition);
        assertTrue(new UntilFormula(new NotFormula(stateReachedByMoving), stateReachedByMoving)
                .appliesTo(gameStates.get(0)));
        assertTrue(new UntilFormula(new NotFormula(stateReachedByMoving),
                new NextFormula(new NotFormula(stateReachedByMoving))).appliesTo(gameStates.get(0)));
    }

    /**
     * Tests that before paule makes any move he always drops a grain.
     */
    @Test
    public void testComplexFormula1() {
        testEnvironment.runGame();
        final ObservableList<GameState> gameStates = testEnvironment.getGameStates();
        final BasicPredicate stateReachedByMoving = new BasicPredicate(gameStates, stateWasReachedByMovingCondition);
        final BasicPredicate stateReachedByDropping = new BasicPredicate(gameStates,
                stateWasReachedByDroppingAGrainCondition);
        final LTLFormula complex = new GloballyFormula(
                new ImpliesFormula(stateReachedByDropping, new NextFormula(stateReachedByMoving)));
        assertTrue(complex.appliesTo(gameStates.get(0)));
    }

    /**
     * Tests that once paule has reached the target tile he stays on that tile until the game ends.
     */
    @Test
    public void testComplexFormula2() {
        testEnvironment.runGame();
        final ObservableList<GameState> gameStates = testEnvironment.getGameStates();
        final BasicPredicate pauleIsOnTarget = new BasicPredicate(gameStates, pauleIsOnTargetCondition);
        final LTLFormula complex = new UntilFormula(new NotFormula(pauleIsOnTarget),
                new GloballyFormula(pauleIsOnTarget));
        assertTrue(complex.appliesTo(gameStates.get(0)));
    }

    /**
     * Tests that overall paule executed 5 times move.
     */
    @Test
    public void testFiveMoves() {
        testEnvironment.runGame();
        final ObservableList<GameState> gameStates = testEnvironment.getGameStates();
        final BasicPredicate stateReachedByMoving = new BasicPredicate(gameStates, stateWasReachedByMovingCondition);
        assertEquals(ROWS_WALKED, stateReachedByMoving.getMatchingStates().size());
        LTLFormula check = stateReachedByMoving;
        for (int i = 0; i < 1; i++) {
            check = new UntilFormula(new NotFormula(stateReachedByMoving), new NextFormula(check));
        }
        assertTrue(check.appliesTo(gameStates.get(0)));
    }

    /**
     * Tests that overall paule executed 6 times move. This test should fail.
     */
    @Test
    public void testWrongHamsterGame() {
        testEnvironment.runGame();
        final ObservableList<GameState> gameStates = testEnvironment.getGameStates();
        final BasicPredicate stateReachedByMoving = new BasicPredicate(gameStates, stateWasReachedByMovingCondition);
        assertThrows(StateCheckException.class, () -> {
            LTLFormula check = stateReachedByMoving;
            for (int i = 0; i < ROWS_WALKED + 1; i++) {
                check = new FinallyFormula(check);
            }
            StateCheckException.checkAndThrow(check, gameStates.get(0),
                    "Paule was expected to move 4 times during the whole game's execution");
        });
    }
}
