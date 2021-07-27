package de.unistuttgart.iste.sqa.oo.hamstersimulator.testframework.ltl;

import de.unistuttgart.iste.sqa.oo.hamstersimulator.testframework.gamestate.GameState;
import de.unistuttgart.iste.sqa.utils.Preconditions;

/**
 * Implementation of a logical implication. The formula evaluates to true
 * if either the first formula evaluates to false for the given state or
 * if the second formula is true for the given state.
 * @author Steffen Becker
 *
 */
public final class ImpliesFormula extends BinaryLTLFormula {

    /**
     * Create a new instance of the implies operator. The order of the operands is important as
     * implies is non-commutative.
     * @param first First Operand, must not be null.
     * @param second Second Operand, must not be null.
     * @param message Message of the formula.
     */
    public ImpliesFormula(final LTLFormula first, final LTLFormula second, final String message) {
        super(first, second, message);
    }

    /**
     * Create a new instance of the implies operator using a default message. The order of the operands is important as
     * implies is non-commutative.
     * @param first First Operand, must not be null.
     * @param second Second Operand, must not be null.
     */
    public ImpliesFormula(final LTLFormula first, final LTLFormula second) {
        this(first, second, "(" + first.getMessage() + ") ==> (" + second.getMessage() + ")");
    }

    @Override
    public boolean appliesTo(final GameState state) {
        Preconditions.checkNotNull(state);
        return !this.getFirstOperand().appliesTo(state) || this.getSecondOperand().appliesTo(state);
    }
}
