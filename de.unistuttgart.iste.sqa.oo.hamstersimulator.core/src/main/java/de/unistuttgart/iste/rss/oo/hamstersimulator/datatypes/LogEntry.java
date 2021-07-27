package de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes;

import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.ObservableLogEntry;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.CommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.hamster.GameHamster;
import de.unistuttgart.iste.sqa.utils.Preconditions;

/**
 * A log entry object represents a log message of the
 * hamster simulator and is typically displayed in the log
 * view at the right side of the simulator window.
 *
 * @author Steffen Becker
 */
public class LogEntry implements ObservableLogEntry {

    /**
     * Immutable message of this log entry.
     */
    private final String message;

    /**
     * Immutable reference to the hamster which logged that entry.
     */
    private final GameHamster hamster;

    /**
     * The immutable command specification that caused this log entry
     */
    private final CommandSpecification specification;

    /**
     * Create a new log entry.
     *
     * @param loggingHamster Reference to the hamster which created the log message.
     *                       Can be null if the log does not originate from a hamster but from the environment.
     * @param loggedMessage  The message to be logged. Has not to be null and not to be empty.
     */
    public LogEntry(final CommandSpecification specification, final GameHamster loggingHamster, final String loggedMessage) {
        super();
        Preconditions.checkNotNull(loggedMessage);
        Preconditions.checkArgument(!loggedMessage.equals(""), "Empty message not allowed");
        this.hamster = loggingHamster;
        this.message = loggedMessage;
        this.specification = specification;
    }

    /**
     * @return Return the hamster reference which logged this entry
     */
    @Override
    public GameHamster getHamster() {
        return this.hamster;
    }

    /**
     * @return The log message to be displayed
     */
    @Override
    public String getMessage() {
        return this.message;
    }

    /**
     * The command specification that caused this log entry to be generated.
     *
     * @return An instance of a {@link de.unistuttgart.iste.rss.oo.hamstersimulator.commands.CommandSpecification}
     *         which describes the event this log message represents (it also caused this LogEntry).
     *         This won't be null
     */
    @Override
    public CommandSpecification getCommandSpecification() {
        return this.specification;
    }
}
