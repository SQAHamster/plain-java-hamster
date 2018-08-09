package de.unistuttgart.iste.rss.oo.hamstersimulator.territory.commands;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.CompositeBaseCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.Territory;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.tile.Tile;

public class RemoveContentFromTileCommand extends CompositeBaseCommand<ModifyContentOfTileCommandSpecification> {

    private final Territory territory;

    public RemoveContentFromTileCommand(final Territory territory, final ModifyContentOfTileCommandSpecification spec) {
        super(spec);
        this.territory = territory;
    }

    @Override
    protected void buildBeforeFirstExecution(final CompositeCommandBuilder builder) {
        final Tile tile = this.territory.getTileAt(getSpecification().getLocation());
        builder.add(tile.getRemoveContentCommand(getSpecification().getTileContent()));
    }
}
