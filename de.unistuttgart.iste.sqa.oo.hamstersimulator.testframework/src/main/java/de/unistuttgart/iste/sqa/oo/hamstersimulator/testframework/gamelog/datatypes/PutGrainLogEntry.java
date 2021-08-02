package de.unistuttgart.iste.sqa.oo.hamstersimulator.testframework.gamelog.datatypes;

public class PutGrainLogEntry extends HamsterLogEntry {
    public PutGrainLogEntry(final int hamsterId) {
        super(LogEntryType.PUT_GRAIN, hamsterId);
    }
}
