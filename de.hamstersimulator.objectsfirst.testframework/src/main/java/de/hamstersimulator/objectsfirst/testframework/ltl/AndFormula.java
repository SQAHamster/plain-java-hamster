package de.hamstersimulator.objectsfirst.testframework.ltl;

import de.hamstersimulator.objectsfirst.testframework.gamestate.GameState;
import de.hamstersimulator.objectsfirst.utils.Preconditions;

/**
 * Implementation of a logical and. The formula evaluates to true
 * if both operands are true formulas for the given state.
 * @author Steffen Becker
 *
 */
public final class AndFormula extends BinaryLTLFormula {

    /**
     * Create a new instance of the and operator.
     * @param first First Operand, must not be null.
     * @param second Second Operand, must not be null.
     * @param message Message of this formula.
     */
    public AndFormula(final LTLFormula first, final LTLFormula second, final String message) {
        super(first, second, message);
    }

    /**
     * Create a new instance of the and operator using a default message.
     * @param first First Operand, must not be null.
     * @param second Second Operand, must not be null.
     */
    public AndFormula(final LTLFormula first, final LTLFormula second) {
        this(first, second, "(" + first.getMessage() + ") AND (" + second.getMessage() + ")");
    }

    @Override
    public boolean appliesTo(final GameState state) {
        Preconditions.checkNotNull(state);
        return this.getFirstOperand().appliesTo(state) && this.getSecondOperand().appliesTo(state);
    }

}
