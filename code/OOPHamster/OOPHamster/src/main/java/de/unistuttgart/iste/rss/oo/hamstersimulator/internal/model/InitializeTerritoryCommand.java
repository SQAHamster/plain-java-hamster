package de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model;

import java.awt.Dimension;
import java.util.stream.Stream;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.UnidirectionalUpdatePropertyCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.UnidirectionalUpdatePropertyCommandSpecification.ActionKind;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.commandspecifications.ClearTileCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.commandspecifications.InitializeTerritoryCommandSpecification;

public class InitializeTerritoryCommand extends AbstractTerritoryCompositeBaseCommand<InitializeTerritoryCommandSpecification> {

    public InitializeTerritoryCommand(final Territory territory, final InitializeTerritoryCommandSpecification spec) {
        super(territory, spec);
    }

    @Override
    protected void buildBeforeFirstExecution(final CompositeCommandBuilder builder) {
        if (this.territory.getRowCount() > 0 && this.territory.getColumnCount() > 0) {
            getAllLocations(this.territory.getSize()).forEach(location -> builder.add(new ClearTileCommand(this.territory, new ClearTileCommandSpecification(location))));
            getAllLocations(this.territory.getSize()).map(location -> this.territory.getTileAt(location)).forEach(tile -> builder.add(UnidirectionalUpdatePropertyCommand.createPropertyUpdateCommand(this.territory.tiles, tile, ActionKind.REMOVE)));
        }

        builder.add(UnidirectionalUpdatePropertyCommand.createPropertyUpdateCommand(this.territory.territorySize, this.specification.getDimension(), ActionKind.SET));

        getAllLocations(this.specification.getDimension()).forEach(location -> builder.add(UnidirectionalUpdatePropertyCommand.createPropertyUpdateCommand(this.territory.tiles, new Tile(this.territory, location), ActionKind.ADD)));
    }

    private Stream<Location> getAllLocations(final Dimension territoryDimension) {
        return this.territory.getAllLocationsFromTo(Location.ORIGIN, Location.from(territoryDimension.height-1, territoryDimension.width-1));
    }

}
