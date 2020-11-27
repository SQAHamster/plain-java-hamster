package de.unistuttgart.iste.rss.oo.hamstersimulator.testframework.ltl;

import de.unistuttgart.iste.rss.utils.Preconditions;

abstract class BinaryLTLFormula {

    protected final LTLFormula firstOperand;
    protected final LTLFormula secondOperand;

    BinaryLTLFormula(final LTLFormula first, final LTLFormula second) {
        super();
        Preconditions.checkNotNull(first);
        Preconditions.checkNotNull(second);
        this.firstOperand = first;
        this.secondOperand = second;
    }

}