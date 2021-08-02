package de.unistuttgart.iste.sqa.oo.hamstersimulator.testframework.gamelog.datatypes;

public class TurnLeftLogEntry extends HamsterLogEntry {
    public TurnLeftLogEntry(final int hamsterId) {
        super(LogEntryType.TURN_LEFT, hamsterId);
    }
}
