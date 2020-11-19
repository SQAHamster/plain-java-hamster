package de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.command.specification.hamster;

import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.ObservableGrain;

/**
 * observable version of command specification used to make the hamster put down a grain
 */
public interface ObservablePutGrainCommandSpecification extends ObservableAbstractHamsterCommandSpecification {

    /**
     * Get the grain the hamster put down
     * @return the grain placed on the current tile of the hamster, != null
     */
    ObservableGrain getGrain();

}
