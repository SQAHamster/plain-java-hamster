package de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.Command;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.UnidirectionalUpdatePropertyCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.UnidirectionalUpdatePropertyCommandSpecification.ActionKind;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Direction;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;
import de.unistuttgart.iste.rss.oo.hamstersimulator.model.InitHamsterCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.model.InitHamsterCommandSpecification;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlySetProperty;
import javafx.beans.property.ReadOnlySetWrapper;
import javafx.collections.FXCollections;

public class Hamster extends TileContent {

    /*
     * Static part of class, provides a hamster registry
     */
    private static final ReadOnlySetWrapper<Hamster> hamsterSet = new ReadOnlySetWrapper<Hamster>();

    static {
        hamsterSet.set(FXCollections.observableSet());
    }

    public static ReadOnlySetProperty<Hamster> hamsterSetProperty() {
        return hamsterSet.getReadOnlyProperty();
    }

    private final ReadOnlyObjectWrapper<Direction> direction = new ReadOnlyObjectWrapper<>(this, "direction", Direction.NORTH);
    private final ReadOnlyListWrapper<Grain> grainInMouth = new ReadOnlyListWrapper<>(this, "grainInMouth", FXCollections.observableArrayList());

    /*
     * Constructors
     */
    // TODO: Fixme, make private by using a creational pattern
    public Hamster() {
        super();

        hamsterSet.add(this);
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

    public Direction getDirection() {
        return direction.get();
    }

    public List<Grain> getGrainInMouth() {
        return Collections.unmodifiableList(grainInMouth.get());
    }

    public Territory getCurrentTerritory() {
        return this.getCurrentTile().orElseThrow(IllegalStateException::new).getTerritory();
    }

    /*
     * Commands
     */
    public Command getInitializeHamsterCommand(final Territory territory, final Optional<Location> location,
            final Direction direction, final int grainCount) {
        return new InitHamsterCommand(this, territory, new InitHamsterCommandSpecification(location, direction, grainCount));
    }

    public Command getRemoveGrainCommand(final Grain grain) {
        return UnidirectionalUpdatePropertyCommand.createPropertyUpdateCommand(this.grainInMouth, "grainInMouth", grain, ActionKind.REMOVE);
    }

    public Command getAddGrainCommand(final Grain grain) {
        return UnidirectionalUpdatePropertyCommand.createPropertyUpdateCommand(this.grainInMouth, "grainInMouth", grain, ActionKind.ADD);
    }

    public Command getSetDirectionCommand(final Direction direction) {
        return UnidirectionalUpdatePropertyCommand.createPropertyUpdateCommand(this.direction, "direction", direction, ActionKind.SET);
    }

    public Command getSetCurrentTileCommand(final Optional<Tile> newTile) {
        return UnidirectionalUpdatePropertyCommand.createPropertyUpdateCommand(this.currentTile, "currentTile", newTile, ActionKind.SET);
    }

    /*
     * OO-Design Methods
     */
    @Override
    public String toString() {
        return "Hamster";
    }

    @Override
    protected boolean blocksEntrance() {
        return false;
    }
}