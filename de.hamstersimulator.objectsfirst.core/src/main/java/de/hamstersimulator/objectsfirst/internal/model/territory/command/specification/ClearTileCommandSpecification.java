package de.hamstersimulator.objectsfirst.internal.model.territory.command.specification;

import de.hamstersimulator.objectsfirst.adapter.observables.command.specification.territory.ObservableClearTileCommandSpecification;
import de.hamstersimulator.objectsfirst.datatypes.Location;

public final class ClearTileCommandSpecification extends AbstractTerritoryTileCommandSpecification implements ObservableClearTileCommandSpecification {

    public ClearTileCommandSpecification(final Location location) {
        super(location);
    }
}
