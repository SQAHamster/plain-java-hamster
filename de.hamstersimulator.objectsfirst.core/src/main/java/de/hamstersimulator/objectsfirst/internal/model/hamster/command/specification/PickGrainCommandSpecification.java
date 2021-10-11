package de.unistuttgart.iste.sqa.oo.hamstersimulator.internal.model.hamster.command.specification;

import de.unistuttgart.iste.sqa.oo.hamstersimulator.adapter.observables.command.specification.hamster.ObservablePickGrainCommandSpecification;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.internal.model.hamster.GameHamster;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.internal.model.territory.Grain;

public class PickGrainCommandSpecification extends AbstractHamsterCommandSpecification implements ObservablePickGrainCommandSpecification {

    private final Grain grain;

    public PickGrainCommandSpecification(final GameHamster hamster, final Grain grain) {
        super(hamster);
        this.grain = grain;
    }

    @Override
    public Grain getGrain() {
        return this.grain;
    }

}
