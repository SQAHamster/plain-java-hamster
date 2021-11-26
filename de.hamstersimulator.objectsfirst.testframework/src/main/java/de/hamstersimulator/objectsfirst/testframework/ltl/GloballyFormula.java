package de.hamstersimulator.objectsfirst.testframework.ltl;

import de.hamstersimulator.objectsfirst.testframework.gamestate.GameState;
import de.hamstersimulator.objectsfirst.utils.Preconditions;

import java.util.Optional;

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
    public Optional<GameState> failsAt(final GameState state) {
        Preconditions.checkNotNull(state);
        GameState current = state;
        while (!current.isFinalState()) {
            final Optional<GameState> failsAtCurrent = getInnerFormula().failsAt(current);
            if (failsAtCurrent.isPresent()) {
                return failsAtCurrent;
            }
            current = current.getNextGameState();
        }
        return getInnerFormula().failsAt(current);
    }
}
