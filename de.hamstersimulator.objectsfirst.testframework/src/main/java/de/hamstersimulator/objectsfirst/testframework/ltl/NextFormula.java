package de.hamstersimulator.objectsfirst.testframework.ltl;

import de.hamstersimulator.objectsfirst.testframework.gamestate.GameState;
import de.hamstersimulator.objectsfirst.utils.Preconditions;

import java.util.Optional;

/**
 * Implementation the temporal next operator. The formula evaluates to the true
 * if the given operand is a true formula when applied to the successor of the given
 * state. If no successor exists, the formula is false.
 * @author Steffen Becker
 *
 */
public final class NextFormula extends UnaryLTLFormula {

    /**
     * Creates a new next operator.
     * @param operand Inner ltl formula, must not be null.
     * @param message Message of this formula.
     */
    public NextFormula(final LTLFormula operand, final String message) {
        super(operand, message);
    }

    /**
     * Creates a new next operator using a default message.
     * @param operand Inner ltl formula, must not be null.
     */
    public NextFormula(final LTLFormula operand) {
        this(operand, "FOR THE NEXT STATE (" + operand.getMessage() + ")");
    }

    @Override
    public Optional<GameState> failsAt(final GameState state) {
        Preconditions.checkNotNull(state);
        if (state.isFinalState()) {
            return Optional.of(state);
        }
        final GameState next = state.getNextGameState();
        return getInnerFormula().failsAt(next);
    }
}
