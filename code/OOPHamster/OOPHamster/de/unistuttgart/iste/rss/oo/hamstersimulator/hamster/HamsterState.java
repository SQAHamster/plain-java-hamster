package de.unistuttgart.iste.rss.oo.hamstersimulator.hamster;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Direction;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.Grain;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.Tile;

public class HamsterState {
    private Optional<Tile> currentTile;
    private Direction direction;
    private final List<Grain> grainInMouth;
    private final List<HamsterStateListener> stateListener = new LinkedList<>();
    private final Hamster hamster;

    public HamsterState(final Hamster hamster, final Tile startTile, final Direction direction,
            final List<Grain> grainInMouth) {
        super();
        this.hamster = hamster;
        this.currentTile = Optional.empty();
        this.direction = direction;
        this.grainInMouth = new LinkedList<>(grainInMouth);
    }

    public Optional<Tile> getCurrentTile() {
        return currentTile;
    }

    public Direction getDirection() {
        return direction;
    }

    public List<Grain> getGrainInMouth() {
        return Collections.unmodifiableList(grainInMouth);
    }

    private class HamsterStateChangerImpl implements HamsterStateChanger {

        /* (non-Javadoc)
         * @see de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.IHamsterStateChanger#setCurrentTile(java.util.Optional)
         */
        @Override
        public void setCurrentTile(final Optional<Tile> newTile) {
            assert HamsterState.this.currentTile.isPresent() && HamsterState.this.currentTile.get().hasObjectInContent(HamsterState.this.hamster);
            assert newTile.isPresent() && newTile.get().canEnter();

            final Optional<Tile> oldTile = HamsterState.this.currentTile;
            oldTile.ifPresent(t -> t.removeObjectFromContent(HamsterState.this.hamster));
            HamsterState.this.currentTile = newTile;
            newTile.ifPresent(t -> t.addObjectToContent(HamsterState.this.hamster));
            fireStateChangedEvent(new HamsterMovedEvent(HamsterState.this.hamster, oldTile, HamsterState.this.currentTile));
        }

        /* (non-Javadoc)
         * @see de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.IHamsterStateChanger#setDirection(de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Direction)
         */
        @Override
        public void setDirection(final Direction direction) {
            final Direction oldDirection = HamsterState.this.direction;
            HamsterState.this.direction = direction;
            fireStateChangedEvent(new HamsterChangedDirectionEvent(HamsterState.this.hamster, oldDirection,
                    HamsterState.this.direction));
        }

        /* (non-Javadoc)
         * @see de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.IHamsterStateChanger#addGrainToMouth(de.unistuttgart.iste.rss.oo.hamstersimulator.territory.Grain)
         */
        @Override
        public void addGrainToMouth(final Grain newGrain) {
            assert HamsterState.this.currentTile.orElseThrow(IllegalStateException::new).hasObjectInContent(newGrain);

            HamsterState.this.grainInMouth.add(newGrain);
            HamsterState.this.currentTile.orElseThrow(IllegalStateException::new).removeObjectFromContent(newGrain);
            fireStateChangedEvent(new HamsterGrainAddedEvent(HamsterState.this.hamster, newGrain));
        }

        /* (non-Javadoc)
         * @see de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.IHamsterStateChanger#removeGrainFromMouth(de.unistuttgart.iste.rss.oo.hamstersimulator.territory.Grain)
         */
        @Override
        public void removeGrainFromMouth(final Grain grainToRemove) {
            assert HamsterState.this.grainInMouth.contains(grainToRemove);

            HamsterState.this.grainInMouth.remove(grainToRemove);
            fireStateChangedEvent(new HamsterGrainDeletedEvent(HamsterState.this.hamster, grainToRemove));
        }

        @Override
        public Hamster getHamster() {
            return HamsterState.this.hamster;
        }

        @Override
        public Grain getAnyGrain() {
            assert HamsterState.this.grainInMouth.size() > 0;
            return HamsterState.this.grainInMouth.get(0);
        }

    }

    HamsterStateChanger getStateChanger() {
        return new HamsterStateChangerImpl();
    }

    void addHamsterStateListener(final HamsterStateListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("No listener object specified");
        }
        this.stateListener.add(listener);
    }

    void removeHamsterStateListener(final HamsterStateListener listener) {
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