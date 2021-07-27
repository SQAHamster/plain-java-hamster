package de.unistuttgart.iste.rss.oo.hamstersimulator.testframework.ltl;

import de.unistuttgart.iste.rss.utils.Preconditions;

/**
 * Abstract base class of all binary ltl operators like and, or, until, ...
 * @author Steffen Becker
 *
 */
abstract class BinaryLTLFormula implements LTLFormula {

    /**
    * Message associated with the formula.
    */
    private final String message;

    /**
     * First operand of this binary operator.
     */
    private final LTLFormula firstOperand;

    /**
     * Second operand of this binary operator.
     */
    private final LTLFormula secondOperand;

    /**
     * Create a new instance of this abstract operator.
     * @param first First Operand, must not be null.
     * @param second Second Operand, must not be null.
     * @param message Message of this formula.
     */
    protected BinaryLTLFormula(final LTLFormula first, final LTLFormula second, final String message) {
        super();
        Preconditions.checkNotNull(first);
        Preconditions.checkNotNull(second);
        Preconditions.checkNotNull(message);

        this.firstOperand = first;
        this.secondOperand = second;
        this.message = message;
    }

    /**
     * @return the firstOperand
     */
    protected LTLFormula getFirstOperand() {
        return firstOperand;
    }

    /**
     * @return the secondOperand
     */
    protected LTLFormula getSecondOperand() {
        return secondOperand;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
