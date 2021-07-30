package de.unistuttgart.iste.sqa.oo.hamstersimulator.testframework.ltl;

import de.unistuttgart.iste.sqa.oo.hamstersimulator.adapter.observables.ObservableHamster;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.adapter.observables.command.specification.ObservableCommandSpecification;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.adapter.observables.command.specification.hamster.ObservableAbstractHamsterCommandSpecification;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.adapter.observables.command.specification.hamster.ObservableMoveCommandSpecification;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.adapter.observables.command.specification.hamster.ObservablePutGrainCommandSpecification;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.datatypes.Direction;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.datatypes.Location;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.testframework.HamsterGameResolver;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.testframework.HamsterTest;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.testframework.gamestate.GameState;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.testframework.gamestate.RecordingHamsterGameTestEnvironment;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Collection;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test to test the functionality of the LTL-based checker.
 * It executes test on the various LTL-operators to check
 * various conditions on the recorded sequence of game states.
 *
 * @author Steffen Becker
 */
@HamsterTest(game = "de.unistuttgart.iste.sqa.oo.hamstersimulator.testframework.ltl.TestSimpleHamsterGame")
@ExtendWith(HamsterGameResolver.class)
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
     * Tests if game states have proper time stamps and are connected correctly.
     *
     * @param testEnvironment The injected test environment used in this test
     */
    @Test
    public void testBasicGameStatePredicate(final RecordingHamsterGameTestEnvironment testEnvironment) {
        testEnvironment.runGame();
        final ObservableList<GameState> gameStates = testEnvironment.getGameStates();
        final GameStatePredicate pauleIsOnOrigin = new GameStatePredicate(gameStates,
                getHamsterOnLocationCondition(getDefaultHamster(testEnvironment), Location.ORIGIN), "Paule is at the origin location.");
        final Collection<GameState> matchingStates = pauleIsOnOrigin.getMatchingStates();
        assertEquals(2, matchingStates.size());
        for (final GameState state : gameStates) {
            assertTrue(!(state.getTimestamp() <= 1) || matchingStates.contains(state));
            assertTrue(!(state.getTimestamp() > 1) || !matchingStates.contains(state));
        }
    }

    /**
     * Tests if game states have proper time stamps and are connected correctly.
     *
     * @param testEnvironment The injected test environment used in this test
     */
    @Test
    public void testFinallyOperator(final RecordingHamsterGameTestEnvironment testEnvironment) {
        testEnvironment.runGame();
        final ObservableList<GameState> gameStates = testEnvironment.getGameStates();
        final GameStatePredicate pauleIsOnTarget = new GameStatePredicate(gameStates,
                getHamsterOnLocationCondition(getDefaultHamster(testEnvironment), FINAL_HAMSTER_LOCATION), "Paule is at his final location (5, 0).");
        assertTrue(new FinallyFormula(pauleIsOnTarget).appliesTo(gameStates.get(0)));
        assertFalse(new NotFormula(new FinallyFormula(pauleIsOnTarget)).appliesTo(gameStates.get(0)));
    }

    /**
     * Tests if game states have proper time stamps and are connected correctly.
     *
     * @param testEnvironment The injected test environment used in this test
     */
    @Test
    public void testGloballyOperator(final RecordingHamsterGameTestEnvironment testEnvironment) {
        testEnvironment.runGame();
        final ObservableList<GameState> gameStates = testEnvironment.getGameStates();
        final GameStatePredicate pauleLooksToSouth = new GameStatePredicate(gameStates,
                getHamsterLooksToCondition(getDefaultHamster(testEnvironment), Direction.SOUTH), "Paule is looking south.");
        assertTrue(new GloballyFormula(pauleLooksToSouth).appliesTo(gameStates.get(0)));
        assertFalse(new NotFormula(new GloballyFormula(pauleLooksToSouth)).appliesTo(gameStates.get(0)));
    }

    /**
     * Tests if game states have proper time stamps and are connected correctly.
     *
     * @param testEnvironment The injected test environment used in this test
     */
    @Test
    public void testUntilOperator(final RecordingHamsterGameTestEnvironment testEnvironment) {
        testEnvironment.runGame();
        final ObservableList<GameState> gameStates = testEnvironment.getGameStates();
        final GameStatePredicate stateReachedByMoving = new GameStatePredicate(gameStates,
                getStateReachedViaCommandCondition(ObservableMoveCommandSpecification.class), "Paule moved with last command.");
        assertTrue(new UntilFormula(new NotFormula(stateReachedByMoving), stateReachedByMoving)
                .appliesTo(gameStates.get(0)));
        assertTrue(new UntilFormula(new NotFormula(stateReachedByMoving),
                new NextFormula(new NotFormula(stateReachedByMoving))).appliesTo(gameStates.get(0)));
    }

    /**
     * Tests that before paule makes any move he always drops a grain.
     *
     * @param testEnvironment The injected test environment used in this test
     */
    @Test
    public void testComplexFormula1(final RecordingHamsterGameTestEnvironment testEnvironment) {
        testEnvironment.runGame();
        final ObservableList<GameState> gameStates = testEnvironment.getGameStates();
        final GameStatePredicate stateReachedByMoving = new GameStatePredicate(gameStates,
                getStateReachedViaCommandCondition(ObservableMoveCommandSpecification.class), "Paule moved with last command.");
        final GameStatePredicate stateReachedByDropping = new GameStatePredicate(gameStates,
                getStateReachedViaCommandCondition(ObservablePutGrainCommandSpecification.class), "Paule put a grain on the tile with last command.");
        final LTLFormula complex = new GloballyFormula(
                new ImpliesFormula(stateReachedByDropping, new NextFormula(stateReachedByMoving)));
        assertTrue(complex.appliesTo(gameStates.get(0)));
    }

    /**
     * Tests that once paule has reached the target tile he stays on that tile until the game ends.
     *
     * @param testEnvironment The injected test environment used in this test
     */
    @Test
    public void testComplexFormula2(final RecordingHamsterGameTestEnvironment testEnvironment) {
        testEnvironment.runGame();
        final ObservableList<GameState> gameStates = testEnvironment.getGameStates();
        final GameStatePredicate pauleIsOnTarget = new GameStatePredicate(gameStates,
                getHamsterOnLocationCondition(getDefaultHamster(testEnvironment), FINAL_HAMSTER_LOCATION), "Paule is on the final location (5, 0).");
        final LTLFormula complex = new UntilFormula(new NotFormula(pauleIsOnTarget),
                new GloballyFormula(pauleIsOnTarget));
        assertTrue(complex.appliesTo(gameStates.get(0)));
    }

    /**
     * Tests that overall paule executed 5 times move. For this test, it does not matter when
     * during the game's execution paule executed the moves. It only matters that it is 5x.
     *
     * @param testEnvironment The injected test environment used in this test
     */
    @Test
    public void testFiveMoves(final RecordingHamsterGameTestEnvironment testEnvironment) {
        testEnvironment.runGame();
        final ObservableList<GameState> gameStates = testEnvironment.getGameStates();
        final GameStatePredicate stateReachedByMoving = new GameStatePredicate(gameStates,
                getStateReachedViaCommandCondition(ObservableMoveCommandSpecification.class), "Paule moved with last command.");
        final GameStatePredicate stateWasChangedByPaule = new GameStatePredicate(gameStates,
                getStateChangedByHamsterCondition(getDefaultHamster(testEnvironment)), "Only paule did an action.");
        final LTLFormula combinedPredicate = new AndFormula(stateReachedByMoving, stateWasChangedByPaule);
        final GameState initialState = gameStates.get(0);
        assertFalse(getNTimesFormula(combinedPredicate, ROWS_WALKED - 1).appliesTo(initialState));
        assertTrue(getNTimesFormula(combinedPredicate, 0).appliesTo(gameStates.get(ROWS_WALKED * 2 + 1)));
        for (int i = 0; i < ROWS_WALKED; i++) {
            assertTrue(getNTimesFormula(combinedPredicate, i + 1).appliesTo(gameStates.get((ROWS_WALKED - i) * 2)));
            assertTrue(getNTimesFormula(combinedPredicate, i + 1).appliesTo(gameStates.get((ROWS_WALKED - i) * 2 - 1)));
            assertFalse(getNTimesFormula(combinedPredicate, i).appliesTo(gameStates.get((ROWS_WALKED - i) * 2)));
            assertFalse(getNTimesFormula(combinedPredicate, i).appliesTo(gameStates.get((ROWS_WALKED - i) * 2 - 1)));
        }
    }

    /**
     * Tests that overall paule executed 6 times move. This test should fail.
     *
     * @param testEnvironment The injected test environment used in this test
     */
    @Test
    public void testWrongHamsterGame(final RecordingHamsterGameTestEnvironment testEnvironment) {
        testEnvironment.runGame();
        final ObservableList<GameState> gameStates = testEnvironment.getGameStates();
        final GameStatePredicate stateReachedByMoving = new GameStatePredicate(gameStates,
                getStateReachedViaCommandCondition(ObservableMoveCommandSpecification.class), "Paule moved with last command.");
        final GameStatePredicate stateWasChangedByPaule = new GameStatePredicate(gameStates,
                getStateChangedByHamsterCondition(getDefaultHamster(testEnvironment)), "Only paule did an action.");
        final LTLFormula combinedPredicate = new AndFormula(stateReachedByMoving, stateWasChangedByPaule);
        assertThrows(StateCheckException.class, () -> {
            StateCheckException.checkOrThrow(getNTimesFormula(combinedPredicate, ROWS_WALKED - 1),
                    gameStates.get(0), "Paule was expected to move exactly 4 times during the whole game's execution");
        });
    }

    /**
     * Creates a ltl formula that evaluates to true if and only if the predicate is valid for exactly
     * repetionCount states until reaching the final state. The formula is composed of two basic parts:
     * 1) a formula that checks after the last occurrence of a state matching the predicate that it does not
     * match any further state. 2) a series of until formulas that match one occurrence of a state matching the provided
     * predicate plus any number of states in front of that state which do not match the given predicate. So basically
     * the sequence of states matched is (no-match-state* match-state)^repetitionCount no-match-state* end-of-sequence
     *
     * @param predicate     The predicate defining the state to be matched repetionCount times
     * @param repetionCount The number of exact matches of the defined state in the sequnce of states against which
     *                      this ltl formula will be evaluated in the end
     * @return A ltl formula matching states fulfilling the predicate repetitonCount times with arbitrary states in-between
     */
    private LTLFormula getNTimesFormula(final LTLFormula predicate, final int repetionCount) {
        LTLFormula check = getNoMatchUntilEndFormula(predicate);
        for (int i = 0; i < repetionCount; i++) {
            check = new UntilFormula(new NotFormula(predicate), predicate.and(new NextFormula(check)));
        }
        return check;
    }

    /**
     * Create a ltl formula which is true if and only if non of the states it evaluates against fulfills the given
     * predicate until the end of the sequence of states is reached.
     *
     * @param predicate The predicate which none of the states has to fulfill
     * @return A ltl formula which is true if no state matches the predicate until the end of the sequence of states.
     */
    private GloballyFormula getNoMatchUntilEndFormula(final LTLFormula predicate) {
        return new GloballyFormula(new NotFormula(predicate));
    }

    private Predicate<GameState> getHamsterLooksToCondition(final ObservableHamster hamster,
                                                            final Direction direction) {
        return state -> state.getHamsterState(hamster).getDirection() == direction;
    }

    private Predicate<GameState> getHamsterOnLocationCondition(final ObservableHamster hamster,
                                                               final Location location) {
        return state -> state.getHamsterState(hamster).getLocation().equals(location);
    }

    private ObservableHamster getDefaultHamster(final RecordingHamsterGameTestEnvironment testEnvironment) {
        return testEnvironment.getViewModel().getTerritory().getDefaultHamster();
    }

    private Predicate<GameState> getStateChangedByHamsterCondition(final ObservableHamster hamster) {
        return state -> {
            if (state.isInitialState()) {
                return false;
            }
            final ObservableCommandSpecification commandSpecification = state.getCommandSpecification().get();
            if (!(commandSpecification instanceof ObservableAbstractHamsterCommandSpecification)) {
                return false;
            }
            final var hamsterCommandSpecification = (ObservableAbstractHamsterCommandSpecification) commandSpecification;
            return hamsterCommandSpecification.getHamster() == hamster;
        };
    }

    private Predicate<GameState> getStateReachedViaCommandCondition(final Class<?> commandClass) {
        return state -> {
            if (state.isInitialState()) {
                return false;
            }
            final ObservableCommandSpecification commandSpecification = state.getCommandSpecification().get();
            return (commandClass.isInstance(commandSpecification));
        };
    }
}
