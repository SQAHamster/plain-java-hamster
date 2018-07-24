package de.unistuttgart.iste.rss.oo.hamster.state;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import de.unistuttgart.iste.rss.oo.hamster.Grain;
import de.unistuttgart.iste.rss.oo.hamster.Hamster;
import de.unistuttgart.iste.rss.oo.hamster.Tile;
import de.unistuttgart.iste.rss.oo.hamster.datatypes.Direction;

public class HamsterState {
    private Tile currentTile;
    private Direction direction;
    private final List<Grain> grainInMouth;
    private final List<HamsterStateListener> stateListener = new LinkedList<>();
    private final Hamster hamster;

    public HamsterState(final Hamster hamster, final Tile startTile, final Direction direction, final List<Grain> grainInMouth) {
        super();
        this.hamster = hamster;
        this.currentTile = startTile;
        this.direction = direction;
        this.grainInMouth = new LinkedList<>(grainInMouth);
    }

    public Tile getCurrentTile() {
        return currentTile;
    }

    public Direction getDirection() {
        return direction;
    }

    public List<Grain> getGrainInMouth() {
        return Collections.unmodifiableList(grainInMouth);
    }

    void setCurrentTile(final Tile newTile) {
        assert this.currentTile.hasObjectInContent(this.hamster);
        assert newTile.canEnter();

        final Tile oldTile = this.currentTile;
        oldTile.removeObjectFromContent(this.hamster);
        this.currentTile = newTile;
        newTile.addObjectToContent(this.hamster);
        fireStateChangedEvent(new HamsterMovedEvent(this.hamster, oldTile, this.currentTile));
    }

    void setDirection(final Direction direction) {
        final Direction oldDirection = this.direction;
        this.direction = direction;
        fireStateChangedEvent(new HamsterChangedDirectionEvent(this.hamster, oldDirection, this.direction));
    }

    void addGrainToMouth(final Grain newGrain) {
        assert this.currentTile.hasObjectInContent(newGrain);

        this.grainInMouth.add(newGrain);
        this.currentTile.removeObjectFromContent(newGrain);
        fireStateChangedEvent(new HamsterGrainAddedEvent(this.hamster, newGrain));
    }

    void removeGrainFromMouth(final Grain grainToRemove) {
        assert this.grainInMouth.contains(grainToRemove);

        this.grainInMouth.remove(grainToRemove);
        fireStateChangedEvent(new HamsterGrainAddedEvent(this.hamster, grainToRemove));
    }

    public void addHamsterStateListener(final HamsterStateListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("No listener object specified");
        }
        this.stateListener.add(listener);
    }

    public void removeHamsterStateListener(final HamsterStateListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("No listener object specified");
        }
        this.stateListener.remove(listener);
    }

    private void fireStateChangedEvent(final HamsterStateChangedEvent event) {
        for (final HamsterStateListener listener : stateListener) {
            listener.onStateChanged(event);
        }
    }
}