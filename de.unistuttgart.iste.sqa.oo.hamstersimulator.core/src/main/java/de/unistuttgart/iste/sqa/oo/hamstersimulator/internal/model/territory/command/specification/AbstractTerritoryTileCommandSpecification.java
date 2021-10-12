package de.unistuttgart.iste.sqa.oo.hamstersimulator.internal.model.territory.command.specification;

import de.unistuttgart.iste.sqa.oo.hamstersimulator.adapter.observables.command.specification.territory.ObservableAbstractTerritoryTileCommandSpecification;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.commands.CommandSpecification;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.datatypes.Location;

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