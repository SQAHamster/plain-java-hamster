package de.unistuttgart.iste.rss.oo.hamstersimulator.testframework.ltl;

import de.unistuttgart.iste.rss.oo.hamstersimulator.testframework.gamestate.GameState;
import de.unistuttgart.iste.rss.utils.Preconditions;

/**
 * Implementation of a temporal release. For the formula to evaluate to true
 * the second operand has to be true until and including the point where
 * the first operand first becomes true; if the first operand never becomes true, the second operand
 * must remain true forever.
 * See <a href="https://en.wikipedia.org/wiki/Linear_temporal_logic">Wikipedia</a> for details.
 * @author Steffen Becker
 *
 */
public final class ReleaseFormula extends BinaryLTLFormula implements LTLFormula {

    /**
     * Create a new instance of the release operator.
     * @param first First Operand, must not be null.
     * @param second Second Operand, must not be null.
     */
    public ReleaseFormula(final LTLFormula first, final LTLFormula second) {
        super(first, second);
    }

    @Override
    public boolean appliesTo(final GameState state) {
        Preconditions.checkNotNull(state);
        GameState current = state;
        while (getSecondOperand().appliesTo(current)) {
            if (current.isFinalState()) {
                return true;
            }
            if (getFirstOperand().appliesTo(current)) {
                return true;
            }
            current = current.getNextGameState();
        }
        return false;
    }

}
