package de.unistuttgart.iste.rss.oo.hamstersimulator.territory.tile.commands;

import java.util.Optional;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.EntityCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.PropertyMap;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.tile.Tile;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.tile.TileContent;

public class SetCurrentTileCommand<T extends TileContent> extends EntityCommand<T> {

    private Optional<Tile> previousTile;
    private final Optional<Tile> newTile;

    public SetCurrentTileCommand(final PropertyMap<T> tileContent, final Optional<Tile> newTile) {
        super(tileContent);
        this.newTile = newTile;
    }

    @Override
    public void execute() {
        this.previousTile = this.entityState.getPropertyOwner().getCurrentTile();
        this.entityState.getObjectProperty("currentTile").set(newTile);
    }

    @Override
    public void undo() {
        this.entityState.getObjectProperty("currentTile").set(previousTile);
    }

}
