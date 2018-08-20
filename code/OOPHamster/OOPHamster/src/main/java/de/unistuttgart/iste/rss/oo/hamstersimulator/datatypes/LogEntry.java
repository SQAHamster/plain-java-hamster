package de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes;

import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.hamster.ReadOnlyHamster;

public class LogEntry {

    private final String message;
    private final ReadOnlyHamster hamster;
    public String getMessage() {
        return message;
    }
    public ReadOnlyHamster getHamster() {
        return hamster;
    }
    public LogEntry(final String message, final ReadOnlyHamster hamster) {
        super();
        this.message = message;
        this.hamster = hamster;
    }
}
