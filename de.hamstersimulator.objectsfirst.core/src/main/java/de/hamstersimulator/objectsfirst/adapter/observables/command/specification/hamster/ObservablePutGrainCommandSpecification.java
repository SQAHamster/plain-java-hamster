package de.hamstersimulator.objectsfirst.adapter.observables.command.specification.hamster;

import de.hamstersimulator.objectsfirst.adapter.observables.ObservableGrain;

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
