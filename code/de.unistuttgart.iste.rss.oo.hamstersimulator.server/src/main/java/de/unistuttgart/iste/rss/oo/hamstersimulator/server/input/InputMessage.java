package de.unistuttgart.iste.rss.oo.hamstersimulator.server.input;

import java.io.Serializable;

import static de.unistuttgart.iste.rss.utils.Preconditions.checkNotNull;

/**
 * Helper class for json serialization, represents a input request including the message
 */
public class InputMessage implements Serializable {
    private static final long serialVersionUID = 3741179009629436658L;

    /**
     * the message of this input message
     */
    private final String message;
    /**
     * the id of the input
     */
    private final int inputId;
    /**
     * the type of the error
     */
    private final String type;
    /**
     * the stacktrace of this input
     * only present if type is
     */
    private final String stacktrace;

    /**
     * Creates a new instance of a InputMessage
     * @param mode  the mode of this input
     * @param message the message to display
     * @param inputId the id of the request
     * @param type the exception type, if it was a showAlert (might be null)
     * @param stacktrace the stacktrace of the exception, if it was a showAlert (might be null)
     */
    private InputMessage(final InputMode mode, final String message, final int inputId,
                        final String type, final String stacktrace) {
        checkNotNull(mode);
        checkNotNull(message);

        this.message = message;
        this.inputId = inputId;
        this.type = type;
        this.stacktrace = stacktrace;
    }

    /**
     * Creates a new instance of a InputMessage
     * @param mode  the mode of this input
     * @param message the message to display
     * @param inputId the id of the request
     */
    private InputMessage(final InputMode mode, final String message, final int inputId) {
        this(mode, message, inputId, null, null);
    }

    /**
     * Creates a new read string input message
     * @param message the message to display
     * @param inputId the id of the request
     * @return the input message, != null
     */
    public static InputMessage GetReadStringMessage(final String message, final int inputId) {
        checkNotNull(message);

        return new InputMessage(InputMode.READ_STRING, message, inputId);
    }

    /**
     * Creates a new read int input message
     * @param message the message to display, must be != null
     * @param inputId the id of the request
     * @return the input message, != null
     */
    public static InputMessage GetReadIntMessage(final String message, final int inputId) {
        checkNotNull(message);

        return new InputMessage(InputMode.READ_INT, message, inputId);
    }

    /**
     * Creates a new confirm alert input message
     * @param message the message to display, must be != null
     * @param inputId the id of the request
     * @param type the exception type, must be != null
     * @param stacktrace the stacktrace of the exception, must be != null
     * @return the message
     */
    public static InputMessage GetConfirmAlertMessage(final String message, final int inputId, final String type,
                                                      final String stacktrace) {
        checkNotNull(message);
        checkNotNull(type);
        checkNotNull(stacktrace);

        return new InputMessage(InputMode.CONFIRM_ALERT, message, inputId, type, stacktrace);
    }

    /**
     * Getter for the inputId
     * @return the id of the input
     */
    public int getInputId() {
        return inputId;
    }
}
