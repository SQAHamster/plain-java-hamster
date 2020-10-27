package de.unistuttgart.iste.rss.oo.hamstersimulator.server.communication.clienttoserver;

import de.unistuttgart.iste.rss.oo.hamstersimulator.server.communication.InputOperation;

public class ReadStringOperation extends InputOperation {
    private static final long serialVersionUID = -3568814463064879901L;

    private final String message;

    public ReadStringOperation(final int inputd, final String message) {
        super(inputId);
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
