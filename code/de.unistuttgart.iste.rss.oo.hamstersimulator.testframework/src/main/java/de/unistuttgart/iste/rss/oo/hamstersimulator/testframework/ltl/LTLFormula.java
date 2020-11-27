package de.unistuttgart.iste.rss.oo.hamstersimulator.testframework.ltl;

import de.unistuttgart.iste.rss.oo.hamstersimulator.testframework.gamestate.GameState;

/**
 * Interface representing a linear temporal logic formula. The only supported
 * Operation is that the formula can be evaluated against a game state (which
 * might include inspecting the successors of that state, too).
 * @author Steffen Becker
 *
 */
public interface LTLFormula {
    /**
     * Apply this ltl formula on the given state and yield the result of that application.
     * @param state The state to check this ltl formula against
     * @return true if the formula holds for the given state
     */
    boolean appliesTo(GameState state);
}
