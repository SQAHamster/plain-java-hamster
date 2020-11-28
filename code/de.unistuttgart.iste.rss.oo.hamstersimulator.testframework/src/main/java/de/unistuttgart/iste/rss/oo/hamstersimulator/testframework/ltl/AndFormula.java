package de.unistuttgart.iste.rss.oo.hamstersimulator.testframework.ltl;

import de.unistuttgart.iste.rss.oo.hamstersimulator.testframework.gamestate.GameState;

/**
 * Implementation of a logical and. The formula evaluates to true
 * if both operands are true formulas for the given state.
 * @author Steffen Becker
 *
 */
public final class AndFormula extends BinaryLTLFormula implements LTLFormula {

    /**
     * Create a new instance of the and operator.
     * @param first First Operand, must not be null.
     * @param second Second Operand, must not be null.
     */
    public AndFormula(final LTLFormula first, final LTLFormula second) {
        super(first, second);
    }

    @Override
    public boolean appliesTo(final GameState state) {
        return this.getFirstOperand().appliesTo(state) & this.getSecondOperand().appliesTo(state);
    }

}
