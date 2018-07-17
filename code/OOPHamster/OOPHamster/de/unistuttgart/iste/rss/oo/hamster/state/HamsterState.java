package de.unistuttgart.iste.rss.oo.hamster.state;

import java.util.List;

import de.unistuttgart.iste.rss.oo.hamster.Direction;
import de.unistuttgart.iste.rss.oo.hamster.Grain;
import de.unistuttgart.iste.rss.oo.hamster.Tile;

public class HamsterState {
    private Tile currentTile;
    private Direction direction;
    private List<Grain> grainInMouth;

    public HamsterState(final Tile startTile, final Direction direction, final List<Grain> grainInMouth) {
        super();
        this.currentTile = startTile;
        this.direction = direction;
        this.grainInMouth = grainInMouth;
    }

    public Tile getCurrentTile() {
        return currentTile;
    }

    public Direction getDirection() {
        return direction;
    }

    public List<Grain> getGrainInMouth() {
        return grainInMouth;
    }

    void setCurrentTile(final Tile currentTile) {
        this.currentTile = currentTile;
    }

    void setDirection(final Direction direction) {
        this.direction = direction;
    }

    void setGrainInMouth(final List<Grain> grainInMouth) {
        this.grainInMouth = grainInMouth;
    }
}