package de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.UnidirectionalUpdatePropertyCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.UnidirectionalUpdatePropertyCommandSpecification.ActionKind;

public class AddWallToTileCommand extends AbstractTerritoryCompositeBaseCommand {

    private final AddWallToTileCommandSpecification specification;

    public AddWallToTileCommand(final Territory territory, final AddWallToTileCommandSpecification spec) {
        super(territory);
        this.specification = spec;
    }

    @Override
    protected void buildBeforeFirstExecution(final CompositeCommandBuilder builder) {
        final Tile tile = this.territory.getTileAt(this.specification.getLocation());
        this.compositeCommandBuilder.add(UnidirectionalUpdatePropertyCommand.createPropertyUpdateCommand(tile.content, "content", new Wall(), ActionKind.ADD));
    }

}
