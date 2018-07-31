package de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.events;

import de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.Hamster;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.Grain;

public class HamsterGrainDeletedEvent extends HamsterStateChangedEvent {

    private final Grain grain;

    public HamsterGrainDeletedEvent(final Hamster hamster, final Grain grain) {
        super(hamster);
        this.grain = grain;
    }

    public Grain getGrain() {
        return grain;
    }

    @Override
    public String toString() {
        return "HamsterGrainDeletedEvent [grain=" + grain + ", toString()=" + super.toString() + "]";
    }
}
