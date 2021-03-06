package de.hamstersimulator.objectsfirst.testframework.ltl;

import de.hamstersimulator.objectsfirst.testframework.gamestate.GameState;
import de.hamstersimulator.objectsfirst.utils.Preconditions;

import java.util.Optional;

/**
 * Implementation of a logical implication. The formula evaluates to true
 * if either the first formula evaluates to false for the given state or
 * if the second formula is true for the given state.
 * @author Steffen Becker
 *
 */
public final class ImpliesFormula extends BinaryLTLFormula {

    /**
     * Create a new instance of the implies operator. The order of the operands is important as
     * implies is non-commutative.
     * @param first First Operand, must not be null.
     * @param second Second Operand, must not be null.
     * @param message Message of the formula.
     */
    public ImpliesFormula(final LTLFormula first, final LTLFormula second, final String message) {
        super(first, second, message);
    }

    /**
     * Create a new instance of the implies operator using a default message. The order of the operands is important as
     * implies is non-commutative.
     * @param first First Operand, must not be null.
     * @param second Second Operand, must not be null.
     */
    public ImpliesFormula(final LTLFormula first, final LTLFormula second) {
        this(first, second, "(" + first.getMessage() + ") ==> (" + second.getMessage() + ")");
    }

    @Override
    public Optional<GameState> failsAt(final GameState state) {
        Preconditions.checkNotNull(state);
        final Optional<GameState> failsAtSecond = this.getSecondOperand().failsAt(state);
        if (failsAtSecond.isPresent() && this.getFirstOperand().appliesTo(state)) {
            return failsAtSecond;
        } else {
            return Optional.empty();
        }
    }
}
