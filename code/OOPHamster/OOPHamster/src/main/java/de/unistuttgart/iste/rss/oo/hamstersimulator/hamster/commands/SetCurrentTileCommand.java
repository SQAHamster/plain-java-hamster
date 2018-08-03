package de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.commands;

import java.util.Optional;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.PropertyMap;
import de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.Hamster;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.Tile;

public class SetCurrentTileCommand extends HamsterCommand {

    private Optional<Tile> previousTile;
    private final Optional<Tile> newTile;

    public SetCurrentTileCommand(final PropertyMap<Hamster> hamsterState, final Optional<Tile> newTile) {
        super(hamsterState);
        this.newTile = newTile;
    }

    @Override
    public void execute() {
        this.previousTile = this.hamster.getCurrentTile();
        this.entityState.getObjectProperty("currentTile").set(newTile);
    }

    @Override
    public void undo() {
        this.entityState.getObjectProperty("currentTile").set(previousTile);
    }

}
