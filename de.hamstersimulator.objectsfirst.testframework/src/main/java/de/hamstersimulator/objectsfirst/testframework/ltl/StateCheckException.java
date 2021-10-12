package de.hamstersimulator.objectsfirst.testframework.ltl;

import de.hamstersimulator.objectsfirst.testframework.gamestate.GameState;
import de.hamstersimulator.objectsfirst.utils.Preconditions;

/**
 * An exception which can be thrown to inform client programs that
 * a given ltl formula expressing a specification of the executed program
 * did not evaluate to true. Objects of this class are immutable. The message
 * tries to be explaining.
 *
 * @author Steffen Becker
 */
public final class StateCheckException extends RuntimeException {

    /**
     * This class' serial UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The ltl formula which failed to be true on the given state.
     */
    private final LTLFormula formula;

    /**
     * The game state on which the ltl formula evaluated to false.
     */
    private final GameState gameState;

    private StateCheckException(final String message, final LTLFormula failedFormula, final GameState stateFailedIn) {
        super(message);
        Preconditions.checkNotNull(message);
        Preconditions.checkArgument(!message.isBlank());
        Preconditions.checkNotNull(failedFormula);
        Preconditions.checkNotNull(stateFailedIn);
        this.formula = failedFormula;
        this.gameState = stateFailedIn;
    }

    /**
     * @return the formula which failed on the given game state.
     */
    public LTLFormula getFormula() {
        return formula;
    }

    /**
     * @return the gameState on which the given ltl formula evaluated to false.
     */
    public GameState getGameState() {
        return gameState;
    }

    /**
     * @param formula   The ltl formula to check against.
     * @param gameState The game state against which the ltl formula is checked.
     * @param message   The user defined message which should describe the intention of the
     *                  ltl formula in natural language.
     *
     * Helper method for writing JUnit tests. The given formula is checked
     * against the given game state. If this check is true, the method returns. Otherwise, a
     * new {@link StateCheckException} is generated and thrown.
     */
    public static void checkOrThrow(final LTLFormula formula, final GameState gameState, final String message) {
        if (!formula.appliesTo(gameState)) {
            final StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("The executed hamster game reached an unexpected state.\n");
            stringBuilder.append("The reason given is:");
            stringBuilder.append(message);
            stringBuilder.append("\n\n");
            stringBuilder.append("Underlying logic:");
            stringBuilder.append(formula.getMessage());
            stringBuilder.append("\n\n");
            throw new StateCheckException(stringBuilder.toString(), formula, gameState);
        }
    }
}
