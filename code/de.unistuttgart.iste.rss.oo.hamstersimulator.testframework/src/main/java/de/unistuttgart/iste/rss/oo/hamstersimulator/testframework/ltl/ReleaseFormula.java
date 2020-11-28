package de.unistuttgart.iste.rss.oo.hamstersimulator.testframework.ltl;

import de.unistuttgart.iste.rss.oo.hamstersimulator.testframework.gamestate.GameState;

/**
 * Implementation of a temporal release. The formula evaluates to true
 * if the second operand applies to the state given and all its successors
 * and eventually the first operand applies too.
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
        GameState current = state;
        while (getSecondOperand().appliesTo(state)) {
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
