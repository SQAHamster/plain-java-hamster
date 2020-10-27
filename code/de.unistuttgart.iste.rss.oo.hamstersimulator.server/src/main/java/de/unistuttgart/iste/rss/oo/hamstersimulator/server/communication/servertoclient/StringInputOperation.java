package de.unistuttgart.iste.rss.oo.hamstersimulator.server.communication.servertoclient;

import de.unistuttgart.iste.rss.oo.hamstersimulator.server.communication.InputOperation;

public class StringInputOperation extends InputOperation {
    private static final long serialVersionUID = 6722980492738342374L;

    private final String result;

    public StringInputOperation(final int inputId, final String result) {
        super(inputId);
        this.result = result;
    }

    public String getResult() {
        return result;
    }
}
