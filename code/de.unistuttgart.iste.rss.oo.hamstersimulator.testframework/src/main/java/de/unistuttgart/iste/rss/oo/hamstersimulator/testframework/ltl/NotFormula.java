package de.unistuttgart.iste.rss.oo.hamstersimulator.testframework.ltl;

import de.unistuttgart.iste.rss.oo.hamstersimulator.testframework.gamestate.GameState;

public final class NotFormula extends UnaryLTLFormula implements LTLFormula {

    public NotFormula(final LTLFormula operand) {
        super(operand);
    }

    @Override
    public boolean appliesTo(final GameState state) {
        return !innerFormula.appliesTo(state);
    }

}
