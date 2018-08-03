package de.unistuttgart.iste.rss.oo.hamstersimulator.territory.tile.commands;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.EntityCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.PropertyMap;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.tile.Tile;

public abstract class TileCommand extends EntityCommand<Tile> {

    public TileCommand(final PropertyMap<Tile> tileState) {
        super(tileState);
    }

    public Tile getTile() {
        return this.entityState.getPropertyOwner();
    }

}
