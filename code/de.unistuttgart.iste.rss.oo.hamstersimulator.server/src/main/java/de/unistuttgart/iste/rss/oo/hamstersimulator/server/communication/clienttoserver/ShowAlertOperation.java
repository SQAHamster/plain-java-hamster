package de.unistuttgart.iste.rss.oo.hamstersimulator.server.communication.clienttoserver;

import de.unistuttgart.iste.rss.oo.hamstersimulator.server.communication.InputOperation;

public class ShowAlertOperation extends InputOperation {

    private static final long serialVersionUID = 7728004034947790199L;

    private final String message;
    private final String exceptionType;
    private final String stacktrace;

    public ShowAlertOperation(final int inputd, final String message, final String exceptionType, final String stacktrace) {
        super(inputId);
        this.message = message;
        this.exceptionType = exceptionType;
        this.stacktrace = stacktrace;
    }

    public String getMessage() {
        return message;
    }

    public String getExceptionType() {
        return exceptionType;
    }

    public String getStacktrace() {
        return stacktrace;
    }
}
