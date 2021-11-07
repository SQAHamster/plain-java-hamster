package de.hamstersimulator.objectsfirst.testframework.gamelog.datatypes;

import com.google.gson.Gson;

import java.util.List;

/**
 * Log representing a complete execution of a game
 *
 * @param territory data representing the initial Territory
 * @param logEntries list of log entries
 */
public record GameLog(TerritoryData territory, List<LogEntry> logEntries) {
    /**
     * Transforms this GameLog into JSON
     * @return a JSON String representing the GameLog
     */
    public String toJson() {
        final Gson gson = new Gson();
        return gson.toJson(this);
    }
}
