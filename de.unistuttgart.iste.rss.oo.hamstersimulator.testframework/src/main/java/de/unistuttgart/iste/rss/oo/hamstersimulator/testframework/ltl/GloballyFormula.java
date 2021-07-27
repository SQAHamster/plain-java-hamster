package de.unistuttgart.iste.rss.oo.hamstersimulator.testframework.ltl;

import de.unistuttgart.iste.rss.oo.hamstersimulator.testframework.gamestate.GameState;
import de.unistuttgart.iste.rss.utils.Preconditions;

/**
 * Implementation the temporal globally operator. The formula evaluates to the true
 * if the given operand is a true formula for the given game state and all its successors.
 * @author Steffen Becker
 *
 */
public final class GloballyFormula extends UnaryLTLFormula {

    /**
     * Creates a new globally operator.
     * @param operand Inner ltl formula, must not be null.
     * @param message Message of this formula.
     */
    public GloballyFormula(final LTLFormula operand, final String message) {
        super(operand, message);
    }

    /**
     * Creates a new globally operator using a default message.
     * @param operand Inner ltl formula, must not be null.
     */
    public GloballyFormula(final LTLFormula operand) {
        this(operand, "FOR THIS AND ALL FUTURE STATES (" + operand.getMessage() + ")");
    }

    @Override
    public boolean appliesTo(final GameState state) {
        Preconditions.checkNotNull(state);
        GameState current = state;
        do {
            if (!getInnerFormula().appliesTo(current)) {
                return false;
            }
            current = current.getNextGameState();
        } while (!current.isFinalState());
        return getInnerFormula().appliesTo(current);
    }
}
