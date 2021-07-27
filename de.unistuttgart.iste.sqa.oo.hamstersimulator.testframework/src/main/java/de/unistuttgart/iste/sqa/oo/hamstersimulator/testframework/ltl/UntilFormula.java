package de.unistuttgart.iste.sqa.oo.hamstersimulator.testframework.ltl;

import de.unistuttgart.iste.sqa.oo.hamstersimulator.testframework.gamestate.GameState;
import de.unistuttgart.iste.sqa.utils.Preconditions;

/**
 * Implementation of a temporal until operator. For the formula to evaluate to true
 * the first operand has to hold at least until the second operand becomes true,
 * which (i.e., that second operand) must hold at the current or a future position.
 * See <a href="https://en.wikipedia.org/wiki/Linear_temporal_logic">Wikipedia</a> for details.
 * @author Steffen Becker
 *
 */
public final class UntilFormula extends BinaryLTLFormula {

    /**
     * Create a new instance of the until operator.
     * @param first First Operand, must not be null.
     * @param second Second Operand, must not be null.
     * @param message Message of this formula.
     */
    public UntilFormula(final LTLFormula first, final LTLFormula second, final String message) {
        super(first, second, message);
    }

    /**
     * Create a new instance of the until operator using a default message.
     * @param first First Operand, must not be null.
     * @param second Second Operand, must not be null.
     */
    public UntilFormula(final LTLFormula first, final LTLFormula second) {
        this(first, second, "(" + first.getMessage() + ") UNTIL (" + second.getMessage() + ")");
    }

    @Override
    public boolean appliesTo(final GameState state) {
        Preconditions.checkNotNull(state);
        GameState current = state;
        // loop invariant: all states starting from state until one state in front of
        // current fulfill first operand and not the second
        while (!getSecondOperand().appliesTo(current)) {
            if (current.isFinalState()) {
                return false;
            }
            if (!getFirstOperand().appliesTo(current)) {
                return false;
            }
            current = current.getNextGameState();
        }
        return true;
    }
}
