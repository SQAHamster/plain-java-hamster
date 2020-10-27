package de.unistuttgart.iste.rss.oo.hamstersimulator.server.communication.clienttoserver;

import de.unistuttgart.iste.rss.oo.hamstersimulator.server.communication.InputOperation;

public class ReadIntegerOperation extends InputOperation {
    private static final long serialVersionUID = -3568814463064879901L;

    private final String message;

    public ReadIntegerOperation(final int inputd, final String message) {
        super(inputId);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
