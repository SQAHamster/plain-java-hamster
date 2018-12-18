package de.unistuttgart.iste.rss.oo.hamstersimulator.external.model;

import java.util.logging.Logger;

/**
 * A specialized hamster which logs all its moves.
 * @author snowball
 *
 */
public class LoggingHamster extends Hamster {

    /**
     * Logger object used to print log messages.
     */
    private final Logger log = Logger.getLogger(LoggingHamster.class.getName());

    /* (non-Javadoc)
     * @see de.unistuttgart.iste.rss.oo.hamstersimulator.external.model.Hamster#move()
     */
    @Override
    public void move() {
        log.fine("Start moving!");
        super.move();
        log.fine("Stop moving!");
    }
}
