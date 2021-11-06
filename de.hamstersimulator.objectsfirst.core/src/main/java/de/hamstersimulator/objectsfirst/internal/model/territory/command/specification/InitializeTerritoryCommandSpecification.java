package de.hamstersimulator.objectsfirst.internal.model.territory.command.specification;

import de.hamstersimulator.objectsfirst.adapter.observables.command.specification.territory.ObservableInitializeTerritoryCommandSpecification;
import de.hamstersimulator.objectsfirst.commands.CommandSpecification;
import de.hamstersimulator.objectsfirst.datatypes.Size;

public final class InitializeTerritoryCommandSpecification implements CommandSpecification, ObservableInitializeTerritoryCommandSpecification {

    private final Size size;

    public InitializeTerritoryCommandSpecification(final Size size) {
        super();
        this.size = size;
    }

    @Override
    public Size getSize() {
        return this.size;
    }
}
