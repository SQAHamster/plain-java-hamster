package de.unistuttgart.iste.rss.oo.hamstersimulator.exceptions;

public class GameAbortedException extends RuntimeException {

    public GameAbortedException(final String message) {
        super(message);
    }

    public GameAbortedException(final String message, final Exception exception) {
        super(message, exception);
    }

    /**
     * Serial ID.
     */
    private static final long serialVersionUID = 4952442615093902161L;

}
