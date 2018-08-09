package de.unistuttgart.iste.rss.oo.hamstersimulator.territory.commands;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.CompositeBaseCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.Territory;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.tile.Grain;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.tile.Tile;

public class AddGrainsToTileCommand extends CompositeBaseCommand<AddGrainsToTileCommandSpecification> {

    private final Territory territory;

    public AddGrainsToTileCommand(final Territory territory, final AddGrainsToTileCommandSpecification spec) {
        super(spec);
        this.territory = territory;
    }

    @Override
    protected void buildBeforeFirstExecution(
            final CompositeBaseCommand<AddGrainsToTileCommandSpecification>.CompositeCommandBuilder builder) {
        final Tile tile = this.territory.getTileAt(getSpecification().getLocation());
        for (int i = 0; i < getSpecification().getAmount(); i++) {
            this.compositeCommandBuilder.add(tile.getAddContentCommand(new Grain()));
        }
    }

}
