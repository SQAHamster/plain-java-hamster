package de.hamstersimulator.objectsfirst.internal.model.territory.command.specification;

import de.hamstersimulator.objectsfirst.adapter.observables.command.specification.territory.ObservableAddWallToTileCommandSpecification;
import de.hamstersimulator.objectsfirst.datatypes.Location;

public final class AddWallToTileCommandSpecification extends AbstractTerritoryTileCommandSpecification implements ObservableAddWallToTileCommandSpecification {

    public AddWallToTileCommandSpecification(final Location location) {
        super(location);
    }

}
