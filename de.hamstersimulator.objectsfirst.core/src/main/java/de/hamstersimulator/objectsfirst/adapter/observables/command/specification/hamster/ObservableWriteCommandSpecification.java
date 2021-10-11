package de.unistuttgart.iste.sqa.oo.hamstersimulator.adapter.observables.command.specification.hamster;

/**
 * observable version of command specification used to print a message to the log via a hamster
 */
public interface ObservableWriteCommandSpecification extends ObservableAbstractHamsterCommandSpecification {

    /**
     * Get the message written to the log
     * @return the message, != null
     */
    String getMessage();

}
