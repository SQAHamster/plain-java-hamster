package de.unistuttgart.iste.rss.oo.hamstersimulator.testframework.ltl;

import de.unistuttgart.iste.rss.oo.hamstersimulator.testframework.gamestate.GameState;

/**
 * Interface representing a linear temporal logic formula. The only supported
 * Operation is that the formula can be evaluated against a game state (which
 * might include inspecting the successors of that state, too).
 * @author Steffen Becker
 *
 */
@FunctionalInterface
public interface LTLFormula {
    /**
     * Apply this ltl formula on the given state and yield the result of that application.
     * @param state The state to check this ltl formula against. This can't be null.
     * @return true if the formula holds for the given state
     */
    boolean appliesTo(GameState state);

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
