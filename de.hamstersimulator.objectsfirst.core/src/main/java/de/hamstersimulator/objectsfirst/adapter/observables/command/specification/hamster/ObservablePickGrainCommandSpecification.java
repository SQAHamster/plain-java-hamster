package de.hamstersimulator.objectsfirst.adapter.observables.command.specification.hamster;

import de.hamstersimulator.objectsfirst.adapter.observables.ObservableGrain;

/**
 * observable version of command specification used to make the hamster pick up one grain
 */
public interface ObservablePickGrainCommandSpecification extends ObservableAbstractHamsterCommandSpecification {

    /**
     * Get the grain the hamster picked up
     * @return the grain the hamster picked up, != null
     */
    ObservableGrain getGrain();

}
