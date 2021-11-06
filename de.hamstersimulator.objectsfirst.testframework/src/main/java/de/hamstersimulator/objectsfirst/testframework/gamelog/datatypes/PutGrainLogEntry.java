package de.hamstersimulator.objectsfirst.testframework.gamelog.datatypes;

public class PutGrainLogEntry extends HamsterLogEntry {
    public PutGrainLogEntry(final int hamsterId) {
        super(LogEntryType.PUT_GRAIN, hamsterId);
    }
}
