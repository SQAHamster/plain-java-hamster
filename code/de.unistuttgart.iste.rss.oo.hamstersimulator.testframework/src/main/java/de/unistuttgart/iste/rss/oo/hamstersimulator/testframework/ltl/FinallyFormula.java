package de.unistuttgart.iste.rss.oo.hamstersimulator.testframework.ltl;

import de.unistuttgart.iste.rss.oo.hamstersimulator.testframework.gamestate.GameState;

public final class FinallyFormula extends UnaryLTLFormula implements LTLFormula {

    FinallyFormula(final LTLFormula operand) {
        super(operand);
    }

    @Override
    public boolean appliesTo(final GameState state) {
        GameState current = state;
        do {
            if (innerFormula.appliesTo(current)) {
                return true;
            }
            current = current.getNextGameState();
        } while (!current.isFinalState());
        assert current.isFinalState();
        return innerFormula.appliesTo(current);
    }

}
