package de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.command.specification.hamster;

import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.ObservableHamster;
import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.command.specification.ObservableCommandSpecification;

/**
 * Base interface for all ObservableCommandSpecification that need a hamster, like
 * ObservableMoveCommandSpecification to move the hamster or
 * ObservableTurnLeftCommandSpecification to make the hamster turn left
 */
public interface ObservableAbstractHamsterCommandSpecification extends ObservableCommandSpecification {

    /**
     * Getter for the hamster that is modified by this command specification
     * @return the hamster, != null
     */
    ObservableHamster getHamster();

}
