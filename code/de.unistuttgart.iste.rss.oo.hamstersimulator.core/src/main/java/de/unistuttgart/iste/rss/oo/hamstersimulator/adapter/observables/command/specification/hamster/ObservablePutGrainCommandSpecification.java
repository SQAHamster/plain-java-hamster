package de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.command.specification.hamster;

import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.ObservableGrain;

public interface ObservablePutGrainCommandSpecification extends ObservableAbstractHamsterCommandSpecification {

    ObservableGrain getGrain();

}
