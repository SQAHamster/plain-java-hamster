package de.unistuttgart.iste.rss.oo.hamstersimulator.testframework.ltl;

import de.unistuttgart.iste.rss.oo.hamstersimulator.testframework.gamestate.GameState;

/**
 * Implementation of a logical or. The formula evaluates to true
 * if one operand is a true formula for the given state.
 * @author Steffen Becker
 *
 */
public final class OrFormula extends BinaryLTLFormula implements LTLFormula {

    OrFormula(final LTLFormula first, final LTLFormula second) {
        super(first, second);
    }

    @Override
    public boolean appliesTo(final GameState state) {
        return this.firstOperand.appliesTo(state) | this.secondOperand.appliesTo(state);
    }

}
