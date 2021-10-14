package de.hamstersimulator.objectsfirst.internal.model.territory.command.specification;

import de.hamstersimulator.objectsfirst.adapter.observables.command.specification.territory.ObservableAbstractTerritoryTileCommandSpecification;
import de.hamstersimulator.objectsfirst.commands.CommandSpecification;
import de.hamstersimulator.objectsfirst.datatypes.Location;

abstract class AbstractTerritoryTileCommandSpecification implements CommandSpecification, ObservableAbstractTerritoryTileCommandSpecification {

    protected final Location location;

    public AbstractTerritoryTileCommandSpecification(final Location location) {
        super();
        this.location = location;
    }

    @Override
    public Location getLocation() {
        return this.location;
    }

}
