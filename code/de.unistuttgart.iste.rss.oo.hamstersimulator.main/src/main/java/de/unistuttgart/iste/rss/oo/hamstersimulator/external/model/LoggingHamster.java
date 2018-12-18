package de.unistuttgart.iste.rss.oo.hamstersimulator.external.model;

import java.util.logging.Logger;

public class LoggingHamster extends Hamster {

    final Logger log = Logger.getLogger(LoggingHamster.class.getName());

    @Override
    public void move() {
        log.fine("Start moving!");
        super.move();
        log.fine("Stop moving!");
    }
}
