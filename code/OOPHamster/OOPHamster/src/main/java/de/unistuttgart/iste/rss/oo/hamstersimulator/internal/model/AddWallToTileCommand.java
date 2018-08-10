package de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.UnidirectionalUpdatePropertyCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.UnidirectionalUpdatePropertyCommandSpecification.ActionKind;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.commandspecifications.AddWallToTileCommandSpecification;

public class AddWallToTileCommand extends AbstractTerritoryCompositeBaseCommand<AddWallToTileCommandSpecification> {

    public AddWallToTileCommand(final Territory territory, final AddWallToTileCommandSpecification spec) {
        super(territory, spec);
    }

    @Override
    protected void buildBeforeFirstExecution(final CompositeCommandBuilder builder) {
        final Tile tile = this.territory.getTileAt(this.specification.getLocation());
        this.compositeCommandBuilder.add(UnidirectionalUpdatePropertyCommand.createPropertyUpdateCommand(tile.content, new Wall(), ActionKind.ADD));
    }

}
