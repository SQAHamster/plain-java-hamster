package de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.events;

import de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.Hamster;

public class HamsterCreatedEvent extends HamsterStateChangedEvent {

    public HamsterCreatedEvent(final Hamster hamster) {
        super(hamster);
    }

}
