package de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.commands;

import java.util.Optional;

import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Direction;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.tile.Tile;

public class InitHamsterCommandParameter {
    private final Optional<Tile> newTile;
    private final Direction newDirection;
    private final int newGrainCount;

    public InitHamsterCommandParameter(final Optional<Tile> newTile, final Direction newDirection, final int newGrainCount) {
        this.newTile = newTile;
        this.newDirection = newDirection;
        this.newGrainCount = newGrainCount;
    }

    public Optional<Tile> getNewTile() {
        return newTile;
    }

    public Direction getNewDirection() {
        return newDirection;
    }

    public int getNewGrainCount() {
        return newGrainCount;
    }
}