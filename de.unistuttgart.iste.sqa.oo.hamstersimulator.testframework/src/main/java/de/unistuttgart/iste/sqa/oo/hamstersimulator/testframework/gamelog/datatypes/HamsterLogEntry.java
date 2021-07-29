package de.unistuttgart.iste.sqa.oo.hamstersimulator.testframework.gamelog.datatypes;

public class HamsterLogEntry extends LogEntry {
    private final int hamsterId;

    protected HamsterLogEntry(final LogEntryType type, final int hamsterId) {
        super(type);
        this.hamsterId = hamsterId;
    }
}
