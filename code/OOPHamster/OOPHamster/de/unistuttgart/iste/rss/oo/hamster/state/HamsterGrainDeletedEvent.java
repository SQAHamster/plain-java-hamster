package de.unistuttgart.iste.rss.oo.hamster.state;

import de.unistuttgart.iste.rss.oo.hamster.Grain;
import de.unistuttgart.iste.rss.oo.hamster.Hamster;

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
