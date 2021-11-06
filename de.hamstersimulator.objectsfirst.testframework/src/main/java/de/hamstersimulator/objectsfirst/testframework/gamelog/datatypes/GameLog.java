package de.hamstersimulator.objectsfirst.testframework.gamelog.datatypes;

import java.util.List;
import java.util.Optional;

public class GameLog {
    private final TerritoryData territory;
    private final List<LogEntry> logEntries;

    public GameLog(final TerritoryData territory, final List<LogEntry> logEntries) {
        this.territory = territory;
        this.logEntries = logEntries;
    }
}
