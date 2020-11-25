package de.unistuttgart.iste.rss.oo.hamstersimulator.testframework.gamestate;

import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.ObservableHamster;

/**
 * Objects of this class store immutably a single message produced by a write command.
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
     * @param newMessage The message which has been written.
     * @param newHamster The hamster who wrote the message.
     */
    public WrittenMessage(final String newMessage, final ObservableHamster newHamster) {
        super();
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
