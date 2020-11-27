package de.unistuttgart.iste.rss.oo.hamstersimulator.testframework.ltl;

abstract class UnaryLTLFormula {

    protected final LTLFormula innerFormula;

    UnaryLTLFormula(final LTLFormula operand) {
        super();
        this.innerFormula = operand;
    }

}