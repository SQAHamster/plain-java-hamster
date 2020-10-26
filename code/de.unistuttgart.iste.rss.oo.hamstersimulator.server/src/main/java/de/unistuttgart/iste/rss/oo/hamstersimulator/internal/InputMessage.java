package de.unistuttgart.iste.rss.oo.hamstersimulator.internal;

/**
 * Helper class for json serialization, represents a input request including the message
 */
public class InputMessage extends LogMessage {

    private final String type;
    private final String stacktrace;

    /**
     * Creates a new instance of a InputMessage
     * @param message the message to display
     * @param id the id of the request
     * @param type the exception type, if it was a showAlert (might be null)
     * @param stacktrace the stacktrace of the exception, if it was a showAlert (might be null)
     */
    public InputMessage(final String message, final int id, final String type, final String stacktrace) {
        super(message, id);
        this.type = type;
        this.stacktrace = stacktrace;
    }

    /**
     * Creates a new instance of a InputMessage
     * @param message the message to display
     * @param id the id of the request
     */
    public InputMessage(final String message, final int id) {
        this(message, id, null, null);
    }
}
