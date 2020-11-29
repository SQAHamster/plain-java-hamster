package de.unistuttgart.iste.rss.oo.hamstersimulator.testframework.ltl;

import de.unistuttgart.iste.rss.oo.hamstersimulator.testframework.gamestate.GameState;
import de.unistuttgart.iste.rss.utils.Preconditions;

/**
 * Implementation the temporal finally operator. The formula evaluates to the true
 * if the given operand is a true formula for the any successor of the given game state
 * or the state itself.
 * @author Steffen Becker
 *
 */
public final class FinallyFormula extends UnaryLTLFormula implements LTLFormula {

    /**
     * Creates a new finally operator.
     * @param operand Inner ltl formula, must not be null.
     */
    public FinallyFormula(final LTLFormula operand) {
        super(operand);
    }

    @Override
    public boolean appliesTo(final GameState state) {
        Preconditions.checkNotNull(state);
        GameState current = state;
        do {
            if (getInnerFormula().appliesTo(current)) {
                return true;
            }
            current = current.getNextGameState();
        } while (!current.isFinalState());
        return getInnerFormula().appliesTo(current);
    }

}
