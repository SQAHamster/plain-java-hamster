package de.unistuttgart.iste.rss.oo.hamstersimulator.testframework.ltl;

import de.unistuttgart.iste.rss.oo.hamstersimulator.testframework.gamestate.GameState;

public final class StateCheckException extends RuntimeException {

    /**
     * This class' serial UID.
     */
    private static final long serialVersionUID = 1L;
    private final LTLFormula formula;
    private final GameState gameState;

    private StateCheckException(final String message, final LTLFormula failedFormula, final GameState stateFailedIn) {
        super(message);
        this.formula = failedFormula;
        this.gameState = stateFailedIn;
    }

    /**
     * @return the formula
     */
    public LTLFormula getFormula() {
        return formula;
    }

    /**
     * @return the gameState
     */
    public GameState getGameState() {
        return gameState;
    }

    public static void checkAndThrow(final LTLFormula formula, final GameState gameState, final String message) {
        if (formula.appliesTo(gameState)) {
            return;
        }
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("The executed hamster game reached an unexpected state\n");
        stringBuilder.append("The reason given is: ");
        stringBuilder.append(message);
        stringBuilder.append("\n\n");
        throw new StateCheckException(stringBuilder.toString(), formula, gameState);
    }
}
