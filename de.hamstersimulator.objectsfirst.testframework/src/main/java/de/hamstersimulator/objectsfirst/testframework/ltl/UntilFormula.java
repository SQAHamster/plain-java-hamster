package de.hamstersimulator.objectsfirst.testframework.ltl;

import de.hamstersimulator.objectsfirst.testframework.gamestate.GameState;
import de.hamstersimulator.objectsfirst.utils.Preconditions;

import java.util.Optional;

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
    public Optional<GameState> failsAt(GameState state) {
        Preconditions.checkNotNull(state);
        GameState current = state;
        Optional<GameState> failsAtCurrent = this.getSecondOperand().failsAt(current);
        // loop invariant: all states starting from state until one state in front of
        // current fulfill first operand and not the second
        while (failsAtCurrent.isPresent()) {
            if (current.isFinalState()) {
                return failsAtCurrent;
            }
            if (!getFirstOperand().appliesTo(current)) {
                return failsAtCurrent;
            }
            current = current.getNextGameState();
            failsAtCurrent = this.getSecondOperand().failsAt(current);
        }
        return Optional.empty();
    }
}
