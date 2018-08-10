package de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.UnidirectionalUpdatePropertyCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.UnidirectionalUpdatePropertyCommandSpecification.ActionKind;

public class AddGrainsToTileCommand extends AbstractTerritoryCompositeBaseCommand {

    private final AddGrainsToTileCommandSpecification specification;

    public AddGrainsToTileCommand(final Territory territory, final AddGrainsToTileCommandSpecification spec) {
        super(territory);
        this.specification = spec;
    }

    @Override
    protected void buildBeforeFirstExecution(final CompositeCommandBuilder builder) {
        final Tile tile = this.territory.getTileAt(this.specification.getLocation());
        for (int i = 0; i < this.specification.getAmount(); i++) {
            this.compositeCommandBuilder.add(UnidirectionalUpdatePropertyCommand.createPropertyUpdateCommand(tile.content, "content", new Grain(), ActionKind.ADD));
        }
    }

}
