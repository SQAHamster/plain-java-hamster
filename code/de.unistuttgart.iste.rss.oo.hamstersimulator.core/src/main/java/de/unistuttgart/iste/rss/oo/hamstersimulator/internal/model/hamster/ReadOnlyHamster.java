package de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.hamster;

import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.ObservableHamster;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Direction;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.territory.Grain;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.territory.ReadOnlyTerritory;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.territory.TileContent;
import javafx.beans.property.*;
import javafx.collections.FXCollections;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class ReadOnlyHamster extends TileContent implements ObservableHamster {

    final ReadOnlyObjectWrapper<Direction> direction = new ReadOnlyObjectWrapper<Direction>(this, "direction", Direction.NORTH);
    final ReadOnlyListWrapper<Grain> grainInMouth = new ReadOnlyListWrapper<Grain>(this, "grainInMouth", FXCollections.observableArrayList());
    final ReadOnlyIntegerWrapper grainCount = new ReadOnlyIntegerWrapper(this, "grainCount", 0);
    final Set<Consumer<Location>> pickGrainHandlers = new HashSet<>();
    final Set<Consumer<Location>> putGrainHandlers = new HashSet<>();

    public ReadOnlyHamster() {
        super();
        this.grainCount.bind(this.grainInMouth.sizeProperty());
    }

    /*@
     @ pure;
     @ requires true;
     @ ensures \result != null;
     @*/

    /**
     * Get the current hamster looking direction.
     *
     * @return The current hamster's looking direction.
     */
    @Override
    public Direction getDirection() {
        return this.direction.get();
    }

    public List<Grain> getGrainInMouth() {
        return Collections.unmodifiableList(this.grainInMouth.get());
    }

    public /*@ pure helper @*/ int getGrainCount() {
        return this.grainCount.get();
    }

    /**
     * Getter for the direction property of the hamster, which represents
     * the direction this hamster is facing
     *
     * @return the property, not null
     */
    @Override
    public ReadOnlyObjectProperty<Direction> directionProperty() {
        return this.direction.getReadOnlyProperty();
    }

    public ReadOnlyListProperty<Grain> grainInMouthProperty() {
        return this.grainInMouth.getReadOnlyProperty();
    }

    public ReadOnlyIntegerProperty grainCountProperty() {
        return this.grainCount.getReadOnlyProperty();
    }

    public ReadOnlyTerritory getCurrentTerritory() {
        if (!this.getCurrentTile().isPresent()) {
            throw new IllegalStateException();
        }
        return this.getCurrentTile().get().getTerritory();
    }

    /*
     * OO-Design Methods
     */
    @Override
    protected boolean blocksEntrance() {
        return false;
    }
}