package de.hamstersimulator.objectsfirst.testframework.ltl;

import de.hamstersimulator.objectsfirst.utils.Preconditions;

/**
 * Abstract base class of all unary ltl operators like next, not, ...
 * @author Steffen Becker
 *
 */
abstract class UnaryLTLFormula implements LTLFormula {

    /**
     * Message associated with the formula.
     */
    private final String message;

    /**
     * Operand of this operator.
     */
    private final LTLFormula innerFormula;

    /**
     * Create a new instance of this abstract operator.
     * @param operand The operand, must not be null.
     * @param message Message of this formula.
     */
    UnaryLTLFormula(final LTLFormula operand, final String message) {
        super();
        Preconditions.checkNotNull(operand);
        Preconditions.checkNotNull(message);

        this.innerFormula = operand;
        this.message = message;
    }

    /**
     * @return the innerFormula
     */
    protected LTLFormula getInnerFormula() {
        return innerFormula;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
