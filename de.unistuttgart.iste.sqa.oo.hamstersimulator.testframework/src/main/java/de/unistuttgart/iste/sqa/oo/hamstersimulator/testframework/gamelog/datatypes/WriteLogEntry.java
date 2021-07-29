package de.unistuttgart.iste.sqa.oo.hamstersimulator.testframework.gamelog.datatypes;

public class WriteLogEntry extends HamsterLogEntry {
    private final String message;

    public WriteLogEntry(final int hamsterId, final String message) {
        super(LogEntryType.PICK_GRAIN, hamsterId);
        this.message = message;
    }
}
