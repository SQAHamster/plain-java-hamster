package de.unistuttgart.iste.rss.oo.hamstersimulator.testframework.ltl;

import de.unistuttgart.iste.rss.utils.Preconditions;

/**
 * Abstract base class of all unary ltl operators like next, not, ...
 * @author Steffen Becker
 *
 */
abstract class UnaryLTLFormula {

    /**
     * Operand of this operator.
     */
    private final LTLFormula innerFormula;

    /**
     * Create a new instance of this abstract operator.
     * @param operand The operand, must not be null.
     */
    UnaryLTLFormula(final LTLFormula operand) {
        super();
        Preconditions.checkNotNull(operand);
        this.innerFormula = operand;
    }

    /**
     * @return the innerFormula
     */
    protected LTLFormula getInnerFormula() {
        return innerFormula;
    }

}
