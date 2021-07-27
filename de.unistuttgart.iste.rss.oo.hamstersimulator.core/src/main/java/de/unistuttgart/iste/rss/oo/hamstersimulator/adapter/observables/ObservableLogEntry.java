package de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables;

import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.command.specification.ObservableCommandSpecification;

/**
 * Interface which provides read-only access to a log entry
 */
public interface ObservableLogEntry {
    /**
     * Every log entries might have a hamster, which can be used to
     * make clear which hamster caused the log message <br>
     * Returns null if the log entry is not linked to a hamster
     *
     * @return the unmodifiable internal hamster, might be null
     */
    ObservableHamster getHamster();

    /**
     * The message is the text which should be displayed in the log
     *
     * @return the message (not null, might be empty)
     */
    String getMessage();

    /**
     * The command specification that caused this log entry to be generated.
     *
     * @return An instance of a {@link de.unistuttgart.iste.rss.oo.hamstersimulator.commands.CommandSpecification}
     *         which describes the event this log message represents (it also caused this LogEntry).
     *         This won't be null
     */
    ObservableCommandSpecification getCommandSpecification();
}
