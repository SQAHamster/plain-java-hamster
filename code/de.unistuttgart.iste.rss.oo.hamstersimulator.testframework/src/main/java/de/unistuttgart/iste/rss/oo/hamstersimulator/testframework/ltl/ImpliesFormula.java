package de.unistuttgart.iste.rss.oo.hamstersimulator.testframework.ltl;

import de.unistuttgart.iste.rss.oo.hamstersimulator.testframework.gamestate.GameState;

/**
 * Implementation of a logical implication. The formula evaluates to true
 * if either the first formula evaluates to false for the given state or
 * if the second formula is true for the given state.
 * @author Steffen Becker
 *
 */
public final class ImpliesFormula extends BinaryLTLFormula implements LTLFormula {

    ImpliesFormula(final LTLFormula first, final LTLFormula second) {
        super(first, second);
    }

    @Override
    public boolean appliesTo(final GameState state) {
        return !this.firstOperand.appliesTo(state) | this.secondOperand.appliesTo(state);
    }

}
