package de.unistuttgart.iste.sqa.oo.hamstersimulator.testframework.gamelog.datatypes;

public class PickGrainLogEntry extends HamsterLogEntry {
    public PickGrainLogEntry(final int hamsterId) {
        super(LogEntryType.PICK_GRAIN, hamsterId);
    }
}

