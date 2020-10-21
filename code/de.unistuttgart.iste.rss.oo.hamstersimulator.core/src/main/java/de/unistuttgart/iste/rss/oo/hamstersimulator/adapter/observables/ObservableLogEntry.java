package de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables;

/**
 * Interface which provides read-only access to a log entry
 */
public interface ObservableLogEntry {
    /**
     * Every log entries might have a hamster, which can be used to
     * make clear which hamster caused the log message <br>
     * Returns null if the log entry is not linked to a hamster
     * @return the unmodifiable internal hamster, might be null
     */
    ObservableHamster getHamster();

    /**
     * The message is the text which should be displayed in the log
     * @return the message (not null, might be empty)
     */
    String getMessage();
}
