package de.hamstersimulator.objectsfirst.testframework.ltl;

import de.hamstersimulator.objectsfirst.adapter.observables.ObservableHamster;
import de.hamstersimulator.objectsfirst.adapter.observables.command.specification.ObservableCommandSpecification;
import de.hamstersimulator.objectsfirst.adapter.observables.command.specification.hamster.ObservableAbstractHamsterCommandSpecification;
import de.hamstersimulator.objectsfirst.datatypes.Direction;
import de.hamstersimulator.objectsfirst.datatypes.Location;
import de.hamstersimulator.objectsfirst.testframework.gamestate.GameState;
import de.hamstersimulator.objectsfirst.testframework.gamestate.RecordingHamsterGameTestEnvironment;

import java.util.function.Predicate;

public class TestBaseClass {
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
    protected LTLFormula getNTimesFormula(final LTLFormula predicate, final int repetionCount) {
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

    protected Predicate<GameState> getHamsterLooksToCondition(final ObservableHamster hamster,
                                                              final Direction direction) {
        return state -> state.getHamsterState(hamster).getDirection() == direction;
    }

    protected Predicate<GameState> getHamsterOnLocationCondition(final ObservableHamster hamster,
                                                                 final Location location) {
        return state -> state.getHamsterState(hamster).getLocation().equals(location);
    }

    protected ObservableHamster getDefaultHamster(final RecordingHamsterGameTestEnvironment testEnvironment) {
        return testEnvironment.getViewModel().getTerritory().getDefaultHamster();
    }

    protected Predicate<GameState> getStateChangedByHamsterCondition(final ObservableHamster hamster) {
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

    protected Predicate<GameState> getStateReachedViaCommandCondition(final Class<?> commandClass) {
        return state -> {
            if (state.isInitialState()) {
                return false;
            }
            final ObservableCommandSpecification commandSpecification = state.getCommandSpecification().get();
            return (commandClass.isInstance(commandSpecification));
        };
    }
}
