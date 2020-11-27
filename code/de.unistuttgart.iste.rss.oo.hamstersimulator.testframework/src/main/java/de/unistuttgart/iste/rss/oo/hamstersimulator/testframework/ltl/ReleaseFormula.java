package de.unistuttgart.iste.rss.oo.hamstersimulator.testframework.ltl;

import de.unistuttgart.iste.rss.oo.hamstersimulator.testframework.gamestate.GameState;

public class ReleaseFormula extends BinaryLTLFormula implements LTLFormula {

    ReleaseFormula(final LTLFormula first, final LTLFormula second) {
        super(first, second);
    }

    @Override
    public boolean appliesTo(final GameState state) {
        GameState current = state;
        while (secondOperand.appliesTo(state)) {
            if (current.isFinalState()) {
                return true;
            }
            if (firstOperand.appliesTo(current)) {
                return true;
            }
            current = current.getNextGameState();
        }
        return false;
    }

}
