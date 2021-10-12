package de.unistuttgart.iste.sqa.oo.hamstersimulator.internal.model.territory.command.specification;

import de.unistuttgart.iste.sqa.oo.hamstersimulator.adapter.observables.command.specification.territory.ObservableClearTileCommandSpecification;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.datatypes.Location;

public final class ClearTileCommandSpecification extends AbstractTerritoryTileCommandSpecification implements ObservableClearTileCommandSpecification {

    public ClearTileCommandSpecification(final Location location) {
        super(location);
    }
}