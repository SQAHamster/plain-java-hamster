package de.unistuttgart.iste.rss.oo.hamstersimulator.testframework.ltl;

import de.unistuttgart.iste.rss.oo.hamstersimulator.testframework.gamestate.GameState;

public class UntilFormula extends BinaryLTLFormula implements LTLFormula {

    UntilFormula(final LTLFormula first, final LTLFormula second) {
        super(first, second);
    }

    @Override
    public boolean appliesTo(final GameState state) {
        GameState current = state;
        while (firstOperand.appliesTo(current)) {
            if (current.isFinalState()) {
                return false;
            }
            current = current.getNextGameState();
        }
        assert !firstOperand.appliesTo(current);
        return secondOperand.appliesTo(current);
    }

}
