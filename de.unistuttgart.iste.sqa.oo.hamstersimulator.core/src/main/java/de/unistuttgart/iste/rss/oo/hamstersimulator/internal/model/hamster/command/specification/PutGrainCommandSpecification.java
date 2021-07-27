package de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.hamster.command.specification;

import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.command.specification.hamster.ObservablePutGrainCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.hamster.GameHamster;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.territory.Grain;

public class PutGrainCommandSpecification extends AbstractHamsterCommandSpecification implements ObservablePutGrainCommandSpecification {

    private final Grain grain;

    public PutGrainCommandSpecification(final GameHamster hamster, final Grain grain) {
        super(hamster);
        this.grain = grain;
    }

    @Override
    public Grain getGrain() {
        return this.grain;
    }

}
