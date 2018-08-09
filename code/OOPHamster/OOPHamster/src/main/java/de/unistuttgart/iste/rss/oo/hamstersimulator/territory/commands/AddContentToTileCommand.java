package de.unistuttgart.iste.rss.oo.hamstersimulator.territory.commands;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.CompositeBaseCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.Territory;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.tile.Tile;

public class AddContentToTileCommand extends CompositeBaseCommand<ModifyContentOfTileCommandSpecification> {

    private final Territory territory;

    public AddContentToTileCommand(final Territory territory, final ModifyContentOfTileCommandSpecification spec) {
        super(spec);
        this.territory = territory;
    }

    @Override
    protected void buildBeforeFirstExecution(final CompositeCommandBuilder builder) {
        final Tile tile = this.territory.getTileAt(getSpecification().getLocation());
        builder.add(tile.getAddContentCommand(getSpecification().getTileContent()));
    }
}
