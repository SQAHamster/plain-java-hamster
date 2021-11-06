package de.hamstersimulator.objectsfirst.testframework.gamelog.datatypes;

public class MoveLogEntry extends HamsterLogEntry {
    public MoveLogEntry(final int hamsterId) {
        super(LogEntryType.MOVE, hamsterId);
    }
}
