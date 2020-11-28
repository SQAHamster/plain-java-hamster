package de.unistuttgart.iste.rss.oo.hamstersimulator.testframework.ltl;

import de.unistuttgart.iste.rss.oo.hamstersimulator.testframework.gamestate.GameState;

/**
 * Implementation of a temporal until operator. The formula evaluates to true
 * if the first operand applies to the given state and its successors until
 * the second formula is a valid formula for a successor or the given state itself.
 * @author Steffen Becker
 *
 */
public final class UntilFormula extends BinaryLTLFormula implements LTLFormula {

    /**
     * Create a new instance of the until operator.
     * @param first First Operand, must not be null.
     * @param second Second Operand, must not be null.
     */
    public UntilFormula(final LTLFormula first, final LTLFormula second) {
        super(first, second);
    }

    @Override
    public boolean appliesTo(final GameState state) {
        GameState current = state;
        while (getFirstOperand().appliesTo(current)) {
            if (current.isFinalState()) {
                return false;
            }
            current = current.getNextGameState();
        }
        assert !getFirstOperand().appliesTo(current);
        return getSecondOperand().appliesTo(current);
    }

}
