package de.unistuttgart.iste.rss.oo.hamstersimulator.testframework.ltl;

import de.unistuttgart.iste.rss.oo.hamstersimulator.testframework.gamestate.GameState;
import de.unistuttgart.iste.rss.utils.Preconditions;

/**
 * Implementation of a logical implication. The formula evaluates to true
 * if either the first formula evaluates to false for the given state or
 * if the second formula is true for the given state.
 * @author Steffen Becker
 *
 */
public final class ImpliesFormula extends BinaryLTLFormula implements LTLFormula {

    /**
     * Create a new instance of the implies operator. The order of the operands is important as
     * implies is non-commutative.
     * @param first First Operand, must not be null.
     * @param second Second Operand, must not be null.
     */
    public ImpliesFormula(final LTLFormula first, final LTLFormula second) {
        super(first, second);
    }

    @Override
    public boolean appliesTo(final GameState state) {
        Preconditions.checkNotNull(state);
        return !this.getFirstOperand().appliesTo(state) || this.getSecondOperand().appliesTo(state);
    }

}
