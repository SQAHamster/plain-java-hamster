package de.hamstersimulator.objectsfirst.adapter.observables.command.specification;

/**
 * Read-only command specification, base interface for all other ObservableCommandSpecifications
 * By itself, it does not provide anything to observe, however all game entities used by subinterfaces
 * (like ObservableMoveCommandSpecification which provides an ObservableHamster) use the observable versions. <br>
 * A common use case for ObservableCommandSpecifications are tests: ObservableLogEntry provides an
 * ObservableCommandSpecification which can be used to find out after the execution what happened while
 * executing the game. An example of this use case can be found at ObservableCommandSpecificationTest in the main module
 */
public interface ObservableCommandSpecification {
}
