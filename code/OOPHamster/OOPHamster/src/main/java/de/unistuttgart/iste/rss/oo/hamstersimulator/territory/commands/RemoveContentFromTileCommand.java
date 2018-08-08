package de.unistuttgart.iste.rss.oo.hamstersimulator.territory.commands;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.CompositeBaseCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.Territory;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.tile.Tile;

public class RemoveContentFromTileCommand extends CompositeBaseCommand {

    private final ModifyContentOfTileCommandParameter spec;
    private final Territory territory;

    public RemoveContentFromTileCommand(final Territory territory, final ModifyContentOfTileCommandParameter spec) {
        super();
        this.territory = territory;
        this.spec = spec;
    }

    @Override
    protected void buildBeforeFirstExecution(final CompositeCommandBuilder builder) {
        final Tile tile = this.territory.getTileAt(this.spec.getLocation());
        builder.add(tile.getRemoveContentCommand(this.spec.getTileContent()));
    }
}
