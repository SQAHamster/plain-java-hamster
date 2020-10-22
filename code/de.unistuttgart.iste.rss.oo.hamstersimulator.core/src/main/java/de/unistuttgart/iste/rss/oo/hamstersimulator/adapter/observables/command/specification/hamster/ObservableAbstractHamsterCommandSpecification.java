package de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.command.specification.hamster;

import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.ObservableHamster;
import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.command.specification.ObservableCommandSpecification;

public interface ObservableAbstractHamsterCommandSpecification extends ObservableCommandSpecification {

    public ObservableHamster getHamster();

}
