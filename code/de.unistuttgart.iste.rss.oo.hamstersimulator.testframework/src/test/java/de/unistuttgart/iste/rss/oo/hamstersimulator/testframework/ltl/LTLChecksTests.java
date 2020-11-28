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
import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.command.specification.ObservableCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.command.specification.hamster.ObservableAbstractHamsterCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.command.specification.hamster.ObservableMoveCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.command.specification.hamster.ObservablePutGrainCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Direction;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;
import de.unistuttgart.iste.rss.oo.hamstersimulator.testframework.gamestate.GameState;
import de.unistuttgart.iste.rss.oo.hamstersimulator.testframework.gamestate.RecordingHamsterGameTestEnvironment;
import javafx.collections.ObservableList;

/**
 * Test to test the functionality of the LTL-based checker.
 * It executes test on the various LTL-operators to check
 * various conditions on the recorded sequence of game states.
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

    /**
     * Predicate that marks all states in which paule is on the territory origin.
     */
    private final Predicate<GameState> pauleIsOnOriginCondition = state -> state.getHamsterState(paule).getLocation()
            .equals(Location.ORIGIN);

    /**
     * Predicate that marks all states in which paule is on his target state.
     */
    private final Predicate<GameState> pauleIsOnTargetCondition = state -> state.getHamsterState(paule).getLocation()
            .equals(FINAL_HAMSTER_LOCATION);

    /**
     * Predicate that marks all states in which paule looks to the south.
     */
    private final Predicate<GameState> pauleLooksToSouthCondition = state -> state.getHamsterState(paule)
            .getDirection() == Direction.SOUTH;

    /**
     * Predicate which marks all states which have been reached from their
     * previous state by executing a putGrain command.
     */
    private final Predicate<GameState> stateWasReachedByDroppingAGrainCondition = state -> {
        if (state.isInitialState()) {
            return false;
        }
        final ObservableCommandSpecification commandSpecification = state.getCommandSpecification().get();
        return (commandSpecification instanceof ObservablePutGrainCommandSpecification);
    };

    /**
     * Predicate which marks all states which have been reached from their
     * previous state by executing a command on paule.
     */
    private final Predicate<GameState> stateWasChangedByPauleCondition = state -> {
        if (state.isInitialState()) {
            return false;
        }
        final ObservableCommandSpecification commandSpecification = state.getCommandSpecification().get();
        if (!(commandSpecification instanceof ObservableAbstractHamsterCommandSpecification)) {
            return false;
        }
        final var hamsterCommandSpecification = (ObservableAbstractHamsterCommandSpecification) commandSpecification;
        return hamsterCommandSpecification.getHamster() == paule;
    };

    /**
     * Predicate which marks all states which have been reached from their
     * previous state by executing a move command.
     */
    private final Predicate<GameState> stateWasReachedByMovingCondition = state -> {
        if (state.isInitialState()) {
            return false;
        }
        final ObservableCommandSpecification commandSpecification = state.getCommandSpecification().get();
        return (commandSpecification instanceof ObservableMoveCommandSpecification);
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
     * Tests that overall paule executed 5 times move. For this test, it does not matter when
     * during the game's execution paule executed the moves. It only matters that it is 5x.
     */
    @Test
    public void testFiveMoves() {
        testEnvironment.runGame();
        final ObservableList<GameState> gameStates = testEnvironment.getGameStates();
        final BasicPredicate stateReachedByMoving = new BasicPredicate(gameStates, stateWasReachedByMovingCondition);
        final BasicPredicate stateWasChangedByPaule = new BasicPredicate(gameStates, stateWasChangedByPauleCondition);
        final LTLFormula combindedPredicate = new AndFormula(stateReachedByMoving, stateWasChangedByPaule);
        assertFalse(getNTimesFormula(combindedPredicate, ROWS_WALKED - 1).appliesTo(gameStates.get(0)));
        assertTrue(getNTimesFormula(combindedPredicate, ROWS_WALKED).appliesTo(gameStates.get(0)));
        assertFalse(getNTimesFormula(combindedPredicate, ROWS_WALKED + 1).appliesTo(gameStates.get(0)));
    }

    /**
     * Tests that overall paule executed 6 times move. This test should fail.
     */
    @Test
    public void testWrongHamsterGame() {
        testEnvironment.runGame();
        final ObservableList<GameState> gameStates = testEnvironment.getGameStates();
        final BasicPredicate stateReachedByMoving = new BasicPredicate(gameStates, stateWasReachedByMovingCondition);
        final BasicPredicate stateWasChangedByPaule = new BasicPredicate(gameStates, stateWasChangedByPauleCondition);
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
}
