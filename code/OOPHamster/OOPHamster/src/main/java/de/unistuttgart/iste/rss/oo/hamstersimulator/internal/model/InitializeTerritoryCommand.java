package de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model;

import java.util.stream.Stream;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.UnidirectionalUpdatePropertyCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.UnidirectionalUpdatePropertyCommandSpecification.ActionKind;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;

public class InitializeTerritoryCommand extends AbstractTerritoryCompositeBaseCommand {

    private final InitializeTerritoryCommandSpecification specification;

    public InitializeTerritoryCommand(final Territory territory, final InitializeTerritoryCommandSpecification spec) {
        super(territory);
        this.specification = spec;
    }

    @Override
    protected void buildBeforeFirstExecution(final CompositeCommandBuilder builder) {
        getAllLocations().forEach(location -> builder.add(new ClearTileCommand(this.territory, new ClearTileCommandSpecification(location))));
        getAllLocations().map(location -> this.territory.getTileAt(location)).forEach(tile -> builder.add(UnidirectionalUpdatePropertyCommand.createPropertyUpdateCommand(this.territory.tiles, "tiles", tile, ActionKind.REMOVE)));

        builder.add(UnidirectionalUpdatePropertyCommand.createPropertyUpdateCommand(this.territory.territorySize, "territorySize", this.specification.dimension, ActionKind.SET));

        getAllLocations().forEach(location -> builder.add(UnidirectionalUpdatePropertyCommand.createPropertyUpdateCommand(this.territory.tiles, "tiles", new Tile(this.territory, location), ActionKind.ADD)));
    }

    private Stream<Location> getAllLocations() {
        return this.territory.getAllLocationsFromTo(Location.ORIGIN, Location.from(this.territory.getRowCount()-1, this.territory.getColumnCount()-1));
    }

}
