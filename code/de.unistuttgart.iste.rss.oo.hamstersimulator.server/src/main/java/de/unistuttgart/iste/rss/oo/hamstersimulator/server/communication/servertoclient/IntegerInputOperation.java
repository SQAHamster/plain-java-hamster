package de.unistuttgart.iste.rss.oo.hamstersimulator.server.communication.servertoclient;

import de.unistuttgart.iste.rss.oo.hamstersimulator.server.communication.InputOperation;

public class IntegerInputOperation extends InputOperation {
    private static final long serialVersionUID = -850005107117889320L;

    private final int result;

    public IntegerInputOperation(final int inputd, int result) {
        super(inputId);
        this.result = result;
    }

    public int getResult() {
        return result;
    }
}
