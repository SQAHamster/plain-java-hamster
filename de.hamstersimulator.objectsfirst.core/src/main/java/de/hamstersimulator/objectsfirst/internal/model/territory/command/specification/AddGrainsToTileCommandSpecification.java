package de.hamstersimulator.objectsfirst.internal.model.territory.command.specification;

import de.hamstersimulator.objectsfirst.adapter.observables.command.specification.territory.ObservableAddGrainsToTileCommandSpecification;
import de.hamstersimulator.objectsfirst.datatypes.Location;

public final class AddGrainsToTileCommandSpecification extends AbstractTerritoryTileCommandSpecification implements ObservableAddGrainsToTileCommandSpecification {

    private final int amount;

    public AddGrainsToTileCommandSpecification(final Location location, final int amount) {
        super(location);
        this.amount = amount;
    }

    @Override
    public int getAmount() {
        return this.amount;
    }

}
