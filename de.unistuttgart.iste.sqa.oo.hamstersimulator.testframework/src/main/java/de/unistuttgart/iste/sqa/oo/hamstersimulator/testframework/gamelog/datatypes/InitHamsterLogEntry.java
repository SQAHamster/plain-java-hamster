package de.unistuttgart.iste.sqa.oo.hamstersimulator.testframework.gamelog.datatypes;

public class InitHamsterLogEntry extends LogEntry {
    private final HamsterData hamster;

    public InitHamsterLogEntry(final HamsterData hamster) {
        super(LogEntryType.INIT_HAMSTER);
        this.hamster = hamster;
    }
}
