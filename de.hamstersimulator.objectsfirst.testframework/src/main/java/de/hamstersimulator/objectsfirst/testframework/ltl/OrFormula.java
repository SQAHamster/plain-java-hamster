package de.hamstersimulator.objectsfirst.testframework.ltl;

import de.hamstersimulator.objectsfirst.testframework.gamestate.GameState;
import de.hamstersimulator.objectsfirst.utils.Preconditions;

import java.util.Optional;

/**
 * Implementation of a logical or. The formula evaluates to true
 * if one operand is a true formula for the given state.
 * @author Steffen Becker
 *
 */
public final class OrFormula extends BinaryLTLFormula {

    /**
     * Create a new instance of the or operator.
     * @param first First Operand, must not be null.
     * @param second Second Operand, must not be null.
     * @param message Message of this formula.
     */
    public OrFormula(final LTLFormula first, final LTLFormula second, final String message) {
        super(first, second, message);
    }

    /**
     * Create a new instance of the or operator using a default message.
     * @param first First Operand, must not be null.
     * @param second Second Operand, must not be null.
     */
    public OrFormula(final LTLFormula first, final LTLFormula second) {
        this(first, second, "(" + first.getMessage() + ") OR (" + second.getMessage() + ")");
    }

    @Override
    public Optional<GameState> failsAt(final GameState state) {
        Preconditions.checkNotNull(state);
        if (this.getFirstOperand().appliesTo(state)) {
            return Optional.empty();
        } else {
            return this.getSecondOperand().failsAt(state);
        }
    }
}
