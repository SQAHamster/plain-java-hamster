package de.unistuttgart.iste.rss.oo.hamstersimulator.testframework.ltl;

import de.unistuttgart.iste.rss.oo.hamstersimulator.testframework.gamestate.GameState;
import de.unistuttgart.iste.rss.utils.Preconditions;

/**
 * Implementation of a logical not. The formula evaluates to true
 * if the operand formula is false for the given state.
 * @author Steffen Becker
 *
 */
public final class NotFormula extends UnaryLTLFormula implements LTLFormula {

    /**
     * Creates a new not operator.
     * @param operand Inner ltl formula, must not be null.
     */
    public NotFormula(final LTLFormula operand) {
        super(operand);
    }

    @Override
    public boolean appliesTo(final GameState state) {
        Preconditions.checkNotNull(state);
        return !getInnerFormula().appliesTo(state);
    }

}
