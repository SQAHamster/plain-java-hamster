package de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes;

import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.hamster.Hamster;

public class LogEntry {

    private final String message;
    private final Hamster hamster;
    public String getMessage() {
        return message;
    }
    public Hamster getHamster() {
        return hamster;
    }
    public LogEntry(final String message, final Hamster hamster) {
        super();
        this.message = message;
        this.hamster = hamster;
    }
}
