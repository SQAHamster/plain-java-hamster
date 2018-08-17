package de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model;

import java.util.stream.Stream;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.UnidirectionalUpdatePropertyCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.specification.UnidirectionalUpdatePropertyCommandSpecification.ActionKind;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Size;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.command.specification.InitializeTerritoryCommandSpecification;

public class InitializeTerritoryCommand extends AbstractTerritoryCompositeBaseCommand<InitializeTerritoryCommandSpecification> {

    public InitializeTerritoryCommand(final Territory territory, final InitializeTerritoryCommandSpecification spec) {
        super(territory, spec);
    }

    @Override
    protected void buildBeforeFirstExecution(final CompositeCommandBuilder builder) {
        if (this.territory.getSize().getRowCount() > 0 && this.territory.getSize().getColumnCount() > 0) {
            getAllLocations(this.territory.getSize()).map(location -> this.territory.getTileAt(location)).forEach(tile -> builder.add(UnidirectionalUpdatePropertyCommand.createPropertyUpdateCommand(this.territory.tiles, tile, ActionKind.REMOVE)));
        }

        builder.add(UnidirectionalUpdatePropertyCommand.createPropertyUpdateCommand(this.territory.territorySize, this.specification.getSize(), ActionKind.SET));

        getAllLocations(this.specification.getSize()).forEach(location -> builder.add(UnidirectionalUpdatePropertyCommand.createPropertyUpdateCommand(this.territory.tiles, new Tile(this.territory, location), ActionKind.ADD)));
    }

    private Stream<Location> getAllLocations(final Size territoryDimension) {
        return Location.getAllLocationsFromTo(Location.ORIGIN, Location.from(territoryDimension.getRowCount()-1, territoryDimension.getColumnCount()-1));
    }

}
