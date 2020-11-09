package de.unistuttgart.iste.rss.oo.hamstersimulator.server.input;

import java.io.Serializable;

/**
 * Helper class for json serialization, represents a input request including the message
 */
public class InputMessage implements Serializable {
    private static final long serialVersionUID = 3741179009629436658L;

    private final String message;
    private final int inputId;
    private final String type;
    private final String stacktrace;

    /**
     * Creates a new instance of a InputMessage
     * @param message the message to display
     * @param inputId the id of the request
     * @param type the exception type, if it was a showAlert (might be null)
     * @param stacktrace the stacktrace of the exception, if it was a showAlert (might be null)
     */
    public InputMessage(final String message, final int inputId, final String type, final String stacktrace) {
        this.message = message;
        this.inputId = inputId;
        this.type = type;
        this.stacktrace = stacktrace;
    }

    /**
     * Creates a new instance of a InputMessage
     * @param message the message to display
     * @param inputId the id of the request
     */
    public InputMessage(final String message, final int inputId) {
        this(message, inputId, null, null);
    }

    public int getInputId() {
        return inputId;
    }

    public String getMessage() {
        return message;
    }

    public String getType() {
        return type;
    }

    public String getStacktrace() {
        return stacktrace;
    }
}
