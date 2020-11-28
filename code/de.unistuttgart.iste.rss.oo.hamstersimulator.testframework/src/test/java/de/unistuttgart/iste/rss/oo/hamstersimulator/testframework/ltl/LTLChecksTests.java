package de.unistuttgart.iste.rss.oo.hamstersimulator.testframework.ltl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.ObservableHamster;
import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.command.specification.ObservableCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.command.specification.hamster.ObservableAbstractHamsterCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.command.specification.hamster.ObservableMoveCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.command.specification.hamster.ObservablePutGrainCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Direction;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;
import de.unistuttgart.iste.rss.oo.hamstersimulator.testframework.HamsterGameResolver;
import de.unistuttgart.iste.rss.oo.hamstersimulator.testframework.HamsterTest;
import de.unistuttgart.iste.rss.oo.hamstersimulator.testframework.gamestate.GameState;
import de.unistuttgart.iste.rss.oo.hamstersimulator.testframework.gamestate.RecordingHamsterGameTestEnvironment;
import javafx.collections.ObservableList;

/**
 * Test to test the functionality of the LTL-based checker.
 * It executes test on the various LTL-operators to check
 * various conditions on the recorded sequence of game states.
 * @author Steffen Becker
 */
@HamsterTest(game = "de.unistuttgart.iste.rss.oo.hamstersimulator.testframework.ltl.TestSimpleHamsterGame")
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
     * @param testEnvironment The injected test environment used in this test
     */
    @Test
    public void testBasicGameStatePredicate(final RecordingHamsterGameTestEnvironment testEnvironment) {
        testEnvironment.runGame();
        final ObservableList<GameState> gameStates = testEnvironment.getGameStates();
        final BasicPredicate pauleIsOnOrigin = new BasicPredicate(gameStates,
                getHamsterOnLocationCondition(getDefaultHamster(testEnvironment), Location.ORIGIN));
        final Collection<GameState> matchingStates = pauleIsOnOrigin.getMatchingStates();
        assertEquals(2, matchingStates.size());
        for (final GameState state : gameStates) {
            assertTrue(!(state.getTimestamp() <= 1) || matchingStates.contains(state));
            assertTrue(!(state.getTimestamp() > 1) || !matchingStates.contains(state));
        }
    }

    /**
     * Tests if game states have proper time stamps and are connected correctly.
     * @param testEnvironment The injected test environment used in this test
     */
    @Test
    public void testFinallyOperator(final RecordingHamsterGameTestEnvironment testEnvironment) {
        testEnvironment.runGame();
        final ObservableList<GameState> gameStates = testEnvironment.getGameStates();
        final BasicPredicate pauleIsOnTarget = new BasicPredicate(gameStates,
                getHamsterOnLocationCondition(getDefaultHamster(testEnvironment), FINAL_HAMSTER_LOCATION));
        assertTrue(new FinallyFormula(pauleIsOnTarget).appliesTo(gameStates.get(0)));
        assertFalse(new NotFormula(new FinallyFormula(pauleIsOnTarget)).appliesTo(gameStates.get(0)));
    }

    /**
     * Tests if game states have proper time stamps and are connected correctly.
     * @param testEnvironment The injected test environment used in this test
     */
    @Test
    public void testGloballyOperator(final RecordingHamsterGameTestEnvironment testEnvironment) {
        testEnvironment.runGame();
        final ObservableList<GameState> gameStates = testEnvironment.getGameStates();
        final BasicPredicate pauleLooksToSouth = new BasicPredicate(gameStates,
                getHamsterLooksToCondition(getDefaultHamster(testEnvironment), Direction.SOUTH));
        assertTrue(new GloballyFormula(pauleLooksToSouth).appliesTo(gameStates.get(0)));
        assertFalse(new NotFormula(new GloballyFormula(pauleLooksToSouth)).appliesTo(gameStates.get(0)));
    }

    /**
     * Tests if game states have proper time stamps and are connected correctly.
      * @param testEnvironment The injected test environment used in this test
    */
    @Test
    public void testUntilOperator(final RecordingHamsterGameTestEnvironment testEnvironment) {
        testEnvironment.runGame();
        final ObservableList<GameState> gameStates = testEnvironment.getGameStates();
        final BasicPredicate stateReachedByMoving = new BasicPredicate(gameStates,
                getStateReachedViaCommandCondition(ObservableMoveCommandSpecification.class));
        assertTrue(new UntilFormula(new NotFormula(stateReachedByMoving), stateReachedByMoving)
                .appliesTo(gameStates.get(0)));
        assertTrue(new UntilFormula(new NotFormula(stateReachedByMoving),
                new NextFormula(new NotFormula(stateReachedByMoving))).appliesTo(gameStates.get(0)));
    }

    /**
     * Tests that before paule makes any move he always drops a grain.
     * @param testEnvironment The injected test environment used in this test
    */
    @Test
    public void testComplexFormula1(final RecordingHamsterGameTestEnvironment testEnvironment) {
        testEnvironment.runGame();
        final ObservableList<GameState> gameStates = testEnvironment.getGameStates();
        final BasicPredicate stateReachedByMoving = new BasicPredicate(gameStates,
                getStateReachedViaCommandCondition(ObservableMoveCommandSpecification.class));
        final BasicPredicate stateReachedByDropping = new BasicPredicate(gameStates,
                getStateReachedViaCommandCondition(ObservablePutGrainCommandSpecification.class));
        final LTLFormula complex = new GloballyFormula(
                new ImpliesFormula(stateReachedByDropping, new NextFormula(stateReachedByMoving)));
        assertTrue(complex.appliesTo(gameStates.get(0)));
    }

    /**
     * Tests that once paule has reached the target tile he stays on that tile until the game ends.
     * @param testEnvironment The injected test environment used in this test
    */
    @Test
    public void testComplexFormula2(final RecordingHamsterGameTestEnvironment testEnvironment) {
        testEnvironment.runGame();
        final ObservableList<GameState> gameStates = testEnvironment.getGameStates();
        final BasicPredicate pauleIsOnTarget = new BasicPredicate(gameStates,
                getHamsterOnLocationCondition(getDefaultHamster(testEnvironment), FINAL_HAMSTER_LOCATION));
        final LTLFormula complex = new UntilFormula(new NotFormula(pauleIsOnTarget),
                new GloballyFormula(pauleIsOnTarget));
        assertTrue(complex.appliesTo(gameStates.get(0)));
    }

    /**
     * Tests that overall paule executed 5 times move. For this test, it does not matter when
     * during the game's execution paule executed the moves. It only matters that it is 5x.
     * @param testEnvironment The injected test environment used in this test
     */
    @Test
    public void testFiveMoves(final RecordingHamsterGameTestEnvironment testEnvironment) {
        testEnvironment.runGame();
        final ObservableList<GameState> gameStates = testEnvironment.getGameStates();
        final BasicPredicate stateReachedByMoving = new BasicPredicate(gameStates,
                getStateReachedViaCommandCondition(ObservableMoveCommandSpecification.class));
        final BasicPredicate stateWasChangedByPaule = new BasicPredicate(gameStates,
                getStateChangedByHamsterCondition(getDefaultHamster(testEnvironment)));
        final LTLFormula combindedPredicate = new AndFormula(stateReachedByMoving, stateWasChangedByPaule);
        assertFalse(getNTimesFormula(combindedPredicate, ROWS_WALKED - 1).appliesTo(gameStates.get(0)));
        assertTrue(getNTimesFormula(combindedPredicate, ROWS_WALKED).appliesTo(gameStates.get(0)));
        assertFalse(getNTimesFormula(combindedPredicate, ROWS_WALKED + 1).appliesTo(gameStates.get(0)));
    }

    /**
     * Tests that overall paule executed 6 times move. This test should fail.
     * @param testEnvironment The injected test environment used in this test
     */
    @Test
    public void testWrongHamsterGame(final RecordingHamsterGameTestEnvironment testEnvironment) {
        testEnvironment.runGame();
        final ObservableList<GameState> gameStates = testEnvironment.getGameStates();
        final BasicPredicate stateReachedByMoving = new BasicPredicate(gameStates,
                getStateReachedViaCommandCondition(ObservableMoveCommandSpecification.class));
        final BasicPredicate stateWasChangedByPaule = new BasicPredicate(gameStates,
                getStateChangedByHamsterCondition(getDefaultHamster(testEnvironment)));
        final LTLFormula combindedPredicate = new AndFormula(stateReachedByMoving, stateWasChangedByPaule);
        assertThrows(StateCheckException.class, () -> {
            StateCheckException.checkAndThrow(getNTimesFormula(combindedPredicate, ROWS_WALKED - 1),
                    gameStates.get(0), "Paule was expected to move exactly 4 times during the whole game's execution");
        });
    }

    private LTLFormula getNTimesFormula(final LTLFormula combindedPredicate, final int timesMoved) {
        LTLFormula check = new GloballyFormula(new NotFormula(combindedPredicate));
        for (int i = 0; i < timesMoved; i++) {
            check = new UntilFormula(new NotFormula(combindedPredicate), new NextFormula(check));
        }
        return check;
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
