package de.unistuttgart.iste.rss.oo.hamstersimulator.territory.tile.commands;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.Command;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.tile.Tile;

public abstract class TileCommand extends Command {

    private final Tile tile;

    public TileCommand(final Tile tile) {
        super();
        this.tile = tile;
    }

    public Tile getTile() {
        return tile;
    }



}
