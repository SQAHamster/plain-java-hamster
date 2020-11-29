package de.unistuttgart.iste.rss.oo.hamstersimulator.testframework.ltl;

import de.unistuttgart.iste.rss.oo.hamstersimulator.testframework.gamestate.GameState;
import de.unistuttgart.iste.rss.utils.Preconditions;

/**
 * Implementation of a logical or. The formula evaluates to true
 * if one operand is a true formula for the given state.
 * @author Steffen Becker
 *
 */
public final class OrFormula extends BinaryLTLFormula implements LTLFormula {

    /**
     * Create a new instance of the or operator.
     * @param first First Operand, must not be null.
     * @param second Second Operand, must not be null.
     */
    public OrFormula(final LTLFormula first, final LTLFormula second) {
        super(first, second);
    }

    @Override
    public boolean appliesTo(final GameState state) {
        Preconditions.checkNotNull(state);
        return this.getFirstOperand().appliesTo(state) || this.getSecondOperand().appliesTo(state);
    }

}
