package de.hamstersimulator.objectsfirst.testframework.ltl;

import de.hamstersimulator.objectsfirst.adapter.observables.ObservableHamster;
import de.hamstersimulator.objectsfirst.adapter.observables.command.specification.ObservableCommandSpecification;
import de.hamstersimulator.objectsfirst.adapter.observables.command.specification.hamster.ObservableAbstractHamsterCommandSpecification;
import de.hamstersimulator.objectsfirst.adapter.observables.command.specification.hamster.ObservableMoveCommandSpecification;
import de.hamstersimulator.objectsfirst.adapter.observables.command.specification.hamster.ObservablePutGrainCommandSpecification;
import de.hamstersimulator.objectsfirst.datatypes.Direction;
import de.hamstersimulator.objectsfirst.datatypes.Location;
import de.hamstersimulator.objectsfirst.testframework.HamsterGameResolver;
import de.hamstersimulator.objectsfirst.testframework.HamsterTest;
import de.hamstersimulator.objectsfirst.testframework.gamestate.GameState;
import de.hamstersimulator.objectsfirst.testframework.gamestate.RecordingHamsterGameTestEnvironment;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Collection;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test to test the functionality of the LTL-based checker if there is only one execution step
 *
 */
@HamsterTest(game = "de.hamstersimulator.objectsfirst.testframework.ltl.OneStateTestSimpleHamsterGame")
@ExtendWith(HamsterGameResolver.class)
public final class OneStateLTLChecksTests extends TestBaseClass {

    /**
     * Tests that the game executes in one step
     * @param testEnvironment The injected test environment used in this test
     */
    @Test
    public void testOneGameState(final RecordingHamsterGameTestEnvironment testEnvironment) {
        testEnvironment.runGame();
        assertEquals(1, testEnvironment.getGameStates().size(), "Should execute in one step");
    }

    /**
     * Tests that the Globally formula works if there is only one execution step
     *
     * @param testEnvironment The injected test environment used in this test
     */
    @Test
    public void testGloballyFormula(final RecordingHamsterGameTestEnvironment testEnvironment) {
        testEnvironment.runGame();
        final ObservableList<GameState> gameStates = testEnvironment.getGameStates();
        final  GameState initialGameState = gameStates.get(0);

        final GameStatePredicate pauleIsOnOrigin = new GameStatePredicate(gameStates,
                getHamsterOnLocationCondition(getDefaultHamster(testEnvironment), Location.ORIGIN), "Paule is at the origin location.");

        final LTLFormula pauleIsGloballyOnOrigin = new GloballyFormula(pauleIsOnOrigin);
        assertTrue(pauleIsGloballyOnOrigin.appliesTo(initialGameState));

        final LTLFormula pauleIsGloballyNotOnOrigin = new GloballyFormula(new NotFormula(pauleIsOnOrigin));
        assertFalse(pauleIsGloballyNotOnOrigin.appliesTo(initialGameState));
    }

    /**
     * Tests that the Finally formula works if there is only one execution step
     *
     * @param testEnvironment The injected test environment used in this test
     */
    @Test
    public void testFinallyFormula(final RecordingHamsterGameTestEnvironment testEnvironment) {
        testEnvironment.runGame();
        final ObservableList<GameState> gameStates = testEnvironment.getGameStates();
        final  GameState initialGameState = gameStates.get(0);

        final GameStatePredicate pauleIsOnOrigin = new GameStatePredicate(gameStates,
                getHamsterOnLocationCondition(getDefaultHamster(testEnvironment), Location.ORIGIN), "Paule is at the origin location.");

        final LTLFormula pauleIsFinallyOnOrigin = new FinallyFormula(pauleIsOnOrigin);
        assertTrue(pauleIsFinallyOnOrigin.appliesTo(initialGameState));

        final LTLFormula pauleIsFinallyNotOnOrigin = new FinallyFormula(new NotFormula(pauleIsOnOrigin));
        assertFalse(pauleIsFinallyNotOnOrigin.appliesTo(initialGameState));
    }


}
