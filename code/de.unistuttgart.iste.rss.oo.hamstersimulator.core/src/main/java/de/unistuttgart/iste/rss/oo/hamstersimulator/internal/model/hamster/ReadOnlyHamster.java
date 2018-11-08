package de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.hamster;

import java.util.Collections;
import java.util.List;

import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Direction;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.territory.Grain;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.territory.ReadOnlyTerritory;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.territory.TileContent;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;

public class ReadOnlyHamster extends TileContent {

    final ReadOnlyObjectWrapper<Direction> direction = new ReadOnlyObjectWrapper<Direction>(this, "direction", Direction.NORTH);
    final ReadOnlyListWrapper<Grain> grainInMouth = new ReadOnlyListWrapper<Grain>(this, "grainInMouth", FXCollections.observableArrayList());
    final ReadOnlyIntegerWrapper grainCount = new ReadOnlyIntegerWrapper(this, "grainCount", 0);

    public ReadOnlyHamster() {
        super();
        grainCount.bind(grainInMouth.sizeProperty());
    }

    /*
     * Read-Only (observable) Properties
     */
    public ReadOnlyObjectProperty<Direction> directionProperty() {
        return this.direction.getReadOnlyProperty();
    }

    public ReadOnlyListProperty<Grain> grainInMouthProperty() {
        return this.grainInMouth.getReadOnlyProperty();
    }

    public ReadOnlyIntegerProperty grainCountProperty() {
        return this.grainCount.getReadOnlyProperty();
    }
    
    public Direction getDirection() {
        return direction.get();
    }

    public List<Grain> getGrainInMouth() {
        return Collections.unmodifiableList(grainInMouth.get());
    }

    public /*@ pure helper @*/ int getGrainCount() {
        return grainCount.get();
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