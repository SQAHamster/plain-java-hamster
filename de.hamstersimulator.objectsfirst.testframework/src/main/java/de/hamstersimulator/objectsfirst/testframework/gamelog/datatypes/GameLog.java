package de.hamstersimulator.objectsfirst.testframework.gamelog.datatypes;

import java.util.List;

/**
 * Log representing a complete execution of a game
 *
 * @param territory data representing the initial Territory
 * @param logEntries list of log entries
 */
public record GameLog(TerritoryData territory, List<LogEntry> logEntries) { }
