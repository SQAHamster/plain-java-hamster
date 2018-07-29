package de.unistuttgart.iste.rss.oo.hamstersimulator.hamster;

import java.util.Optional;

import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.Tile;

public class HamsterMovedEvent extends HamsterStateChangedEvent {

    private final Optional<Tile> oldTile;
    private final Optional<Tile> newTile;

    public HamsterMovedEvent(final Hamster hamster, final Optional<Tile> oldTile, final Optional<Tile> currentTile) {
        super(hamster);
        this.oldTile = oldTile;
        this.newTile = currentTile;
    }

    public Optional<Tile> getOldTile() {
        return oldTile;
    }

    public Optional<Tile> getNewTile() {
        return newTile;
    }

    @Override
    public String toString() {
        return "HamsterMovedEvent [oldTile=" + oldTile + ", newTile=" + newTile + ", toString()=" + super.toString()
        + "]";
    }
}
