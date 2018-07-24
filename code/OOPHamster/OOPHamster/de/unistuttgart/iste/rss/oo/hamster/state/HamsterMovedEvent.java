package de.unistuttgart.iste.rss.oo.hamster.state;

import de.unistuttgart.iste.rss.oo.hamster.Hamster;
import de.unistuttgart.iste.rss.oo.hamster.Tile;

public class HamsterMovedEvent extends HamsterStateChangedEvent {

    private final Tile oldTile;
    private final Tile newTile;

    public HamsterMovedEvent(final Hamster hamster, final Tile oldTile, final Tile newTile) {
        super(hamster);
        this.oldTile = oldTile;
        this.newTile = newTile;
    }

    public Tile getOldTile() {
        return oldTile;
    }

    public Tile getNewTile() {
        return newTile;
    }

    @Override
    public String toString() {
        return "HamsterMovedEvent [oldTile=" + oldTile + ", newTile=" + newTile + ", toString()=" + super.toString()
        + "]";
    }
}
