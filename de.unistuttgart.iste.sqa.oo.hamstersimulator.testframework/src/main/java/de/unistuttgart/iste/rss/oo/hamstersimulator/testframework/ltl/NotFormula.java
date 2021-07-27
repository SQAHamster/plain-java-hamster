package de.unistuttgart.iste.rss.oo.hamstersimulator.testframework.ltl;

import de.unistuttgart.iste.rss.oo.hamstersimulator.testframework.gamestate.GameState;
import de.unistuttgart.iste.sqa.utils.Preconditions;

/**
 * Implementation of a logical not. The formula evaluates to true
 * if the operand formula is false for the given state.
 * @author Steffen Becker
 *
 */
public final class NotFormula extends UnaryLTLFormula {

    /**
     * Creates a new not operator.
     * @param operand Inner ltl formula, must not be null.
     * @param message Message of this formula.
     */
    public NotFormula(final LTLFormula operand, final String message) {
        super(operand, message);
    }

    /**
     * Creates a new not operator using a default message.
     * @param operand Inner ltl formula, must not be null.
     */
    public NotFormula(final LTLFormula operand) {
        this(operand, "NOT (" + operand.getMessage() + ")");
    }

    @Override
    public boolean appliesTo(final GameState state) {
        Preconditions.checkNotNull(state);
        return !getInnerFormula().appliesTo(state);
    }
}
