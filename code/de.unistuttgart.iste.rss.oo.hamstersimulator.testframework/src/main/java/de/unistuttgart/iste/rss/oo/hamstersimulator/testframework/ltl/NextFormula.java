package de.unistuttgart.iste.rss.oo.hamstersimulator.testframework.ltl;

import de.unistuttgart.iste.rss.oo.hamstersimulator.testframework.gamestate.GameState;

/**
 * Implementation the temporal next operator. The formula evaluates to the true
 * if the given operand is a true formula when applied to the successor of the given
 * state. If no successor exists, the formula is false.
 * @author Steffen Becker
 *
 */
public final class NextFormula extends UnaryLTLFormula implements LTLFormula {

    /**
     * Creates a new next operator.
     * @param operand Inner ltl formula, must not be null.
     */
    public NextFormula(final LTLFormula operand) {
        super(operand);
    }

    @Override
    public boolean appliesTo(final GameState state) {
        if (state.isFinalState()) {
            return false;
        }
        final GameState next = state.getNextGameState();
        return getInnerFormula().appliesTo(next);
    }

}
