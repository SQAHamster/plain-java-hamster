package de.unistuttgart.iste.rss.oo.hamstersimulator.territory.tile.commands;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.CompositeBaseCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.tile.Grain;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.tile.Tile;

public class AddGrainsToTileCommand extends CompositeBaseCommand {

    public AddGrainsToTileCommand(final Tile tile, final int amount) {
        super();

        for (int i = 0; i < amount; i++) {
            this.compositeCommandBuilder.add(tile.getAddContentCommand(new Grain()));
        }
    }
}
