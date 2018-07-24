package de.unistuttgart.iste.rss.oo.hamster.state;

import de.unistuttgart.iste.rss.oo.hamster.Grain;
import de.unistuttgart.iste.rss.oo.hamster.Hamster;

public class HamsterGrainAddedEvent extends HamsterStateChangedEvent {

    private final Grain grain;

    public HamsterGrainAddedEvent(final Hamster hamster, final Grain grain) {
        super(hamster);
        this.grain = grain;
    }

    public Grain getGrain() {
        return grain;
    }

    @Override
    public String toString() {
        return "HamsterGrainAddedEvent [grain=" + grain + ", toString()=" + super.toString() + "]";
    }
}
