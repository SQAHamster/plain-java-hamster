package de.unistuttgart.iste.sqa.oo.hamstersimulator.internal.model.territory.command.specification;

import de.unistuttgart.iste.sqa.oo.hamstersimulator.adapter.observables.command.specification.territory.ObservableAddWallToTileCommandSpecification;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.datatypes.Location;

public final class AddWallToTileCommandSpecification extends AbstractTerritoryTileCommandSpecification implements ObservableAddWallToTileCommandSpecification {

    public AddWallToTileCommandSpecification(final Location location) {
        super(location);
    }

}
