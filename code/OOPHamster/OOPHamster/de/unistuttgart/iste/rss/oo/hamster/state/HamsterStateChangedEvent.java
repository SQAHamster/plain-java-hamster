package de.unistuttgart.iste.rss.oo.hamster.state;

import de.unistuttgart.iste.rss.oo.hamster.Hamster;

public abstract class HamsterStateChangedEvent {
    private final Hamster hamster;

    public HamsterStateChangedEvent(final Hamster hamster) {
        super();
        this.hamster = hamster;
    }

    public Hamster getHamster() {
        return hamster;
    }

    @Override
    public String toString() {
        return "HamsterStateChangedEvent [hamster=" + hamster + "]";
    }
}
