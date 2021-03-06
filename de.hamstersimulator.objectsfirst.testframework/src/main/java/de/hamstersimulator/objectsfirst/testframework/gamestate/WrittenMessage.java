package de.hamstersimulator.objectsfirst.testframework.gamestate;

import de.hamstersimulator.objectsfirst.adapter.observables.ObservableHamster;
import de.hamstersimulator.objectsfirst.utils.Preconditions;

/**
 * Objects of this class immutably store a single message produced by a write command on a hamster.
 * @author Steffen Becker
 *
 */
public final class WrittenMessage {

    /**
     * The written message.
     */
    private final String message;

    /**
     *  The hamster who wrote the message.
     */
    private final ObservableHamster hamster;

    /**
     * Constructs a new written message state object.
     * @param newMessage The message which has been written. Has to be non-null and not empty.
     * @param newHamster The hamster who wrote the message. Has to be non-null.
     */
    public WrittenMessage(final String newMessage, final ObservableHamster newHamster) {
        super();
        Preconditions.checkNotNull(newHamster);
        Preconditions.checkNotNull(newMessage);
        Preconditions.checkArgument(!newMessage.isEmpty());
        this.message = newMessage;
        this.hamster = newHamster;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @return the hamster
     */
    public ObservableHamster getHamster() {
        return hamster;
    }
}
