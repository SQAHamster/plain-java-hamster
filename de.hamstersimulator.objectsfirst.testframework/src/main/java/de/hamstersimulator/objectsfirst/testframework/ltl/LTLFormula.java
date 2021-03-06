package de.hamstersimulator.objectsfirst.testframework.ltl;

import de.hamstersimulator.objectsfirst.testframework.gamestate.GameState;

import java.util.Optional;

/**
 * Interface representing a linear temporal logic formula. The only supported
 * Operation is that the formula can be evaluated against a game state (which
 * might include inspecting the successors of that state, too).
 * @author Steffen Becker
 *
 */
public interface LTLFormula {

    //@ public instance invariant \forall GameState state; state != null; appliesTo(state) <==> failsAt(state).isEmpty();

    /**
     * Apply this ltl formula on the given state and yield the result of that application.
     * @param state The state to check this ltl formula against. This can't be null.
     * @return true if the formula holds for the given state
     */
    default boolean appliesTo(GameState state) {
        return this.failsAt(state).isEmpty();
    }

    /**
     * Apply this ltl formula on the given state and yield the state which caused the formula to fail.
     * @param state The state to check this ltl formula against. This can't be null.
     * @return the state where the formula failed, if the formula was successful an empty optional
     */
    Optional<GameState> failsAt(GameState state);

    /**
     * Returns a message that describes the case(s) in which this ltl formula would apply to a {@link GameState}.
     * @return Message describing the ltl formula.
     */
    String getMessage();

    /**
     * @return A new ltl formula which negates the current formula
     */
    default LTLFormula negate() {
        return new NotFormula(this);
    }

    /**
     * @param secondOperand The second operand of the and operator
     * @return A new formula which represents this formula concated with the other
     *         formula via an and operator.
     */
    default LTLFormula and(final LTLFormula secondOperand) {
        return new AndFormula(this, secondOperand);
    }

    /**
     * @param secondOperand The second operand of the and operator
     * @return A new formula which represents this formula concated with the other
     *         formula via an or operator.
     */
    default LTLFormula or(final LTLFormula secondOperand) {
        return new OrFormula(this, secondOperand);
    }

}
