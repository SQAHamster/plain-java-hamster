package de.hamstersimulator.objectsfirst.testframework.ltl;

import de.hamstersimulator.objectsfirst.testframework.gamestate.GameState;
import de.hamstersimulator.objectsfirst.utils.Preconditions;

import java.util.Optional;

/**
 * Implementation of a temporal release. For the formula to evaluate to true
 * the second operand has to be true until and including the point where
 * the first operand first becomes true; if the first operand never becomes true, the second operand
 * must remain true forever.
 * See <a href="https://en.wikipedia.org/wiki/Linear_temporal_logic">Wikipedia</a> for details.
 *
 * @author Steffen Becker
 */
public final class ReleaseFormula extends BinaryLTLFormula {

    /**
     * Create a new instance of the release operator.
     *
     * @param first   First Operand, must not be null.
     * @param second  Second Operand, must not be null.
     * @param message Message of this formula.
     */
    public ReleaseFormula(final LTLFormula first, final LTLFormula second, final String message) {
        super(first, second, message);
    }

    /**
     * Create a new instance of the release operator using a default message.
     *
     * @param first  First Operand, must not be null.
     * @param second Second Operand, must not be null.
     */
    public ReleaseFormula(final LTLFormula first, final LTLFormula second) {
        this(first, second, "(" + second.getMessage() + ") MUST ALWAYS APPLY UNTIL (" + first.getMessage() + ")");
    }

    @Override
    public Optional<GameState> failsAt(final GameState state) {
        Preconditions.checkNotNull(state);
        GameState current = state;
        while (this.getSecondOperand().appliesTo(current)) {
            if (current.isFinalState()) {
                return Optional.empty();
            }
            if (this.getFirstOperand().appliesTo(current)) {
                return Optional.empty();
            }
            current = current.getNextGameState();
        }
        return this.getSecondOperand().failsAt(state);
    }
}
