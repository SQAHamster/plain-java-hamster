package de.unistuttgart.iste.rss.oo.hamstersimulator.testframework.ltl;

import de.unistuttgart.iste.rss.oo.hamstersimulator.testframework.gamestate.GameState;

/**
 * Implementation the temporal globally operator. The formula evaluates to the true
 * if the given operand is a true formula for the given game state and all its successors.
 * @author Steffen Becker
 *
 */
public final class GloballyFormula extends UnaryLTLFormula implements LTLFormula {

    GloballyFormula(final LTLFormula operand) {
        super(operand);
    }

    @Override
    public boolean appliesTo(final GameState state) {
        GameState current = state;
        do {
            if (!innerFormula.appliesTo(current)) {
                return false;
            }
            current = current.getNextGameState();
        } while (!current.isFinalState());
        assert current.isFinalState();
        return innerFormula.appliesTo(current);
    }

}
