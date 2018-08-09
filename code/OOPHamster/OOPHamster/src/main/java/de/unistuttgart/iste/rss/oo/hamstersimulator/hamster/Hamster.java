package de.unistuttgart.iste.rss.oo.hamstersimulator.hamster;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.AbstractBaseCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.PropertyMap;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.UnidirectionalUpdatePropertyCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.specification.PropertyCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.specification.PropertyCommandSpecification.ActionKind;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Direction;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;
import de.unistuttgart.iste.rss.oo.hamstersimulator.simulator.commands.InitHamsterCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.simulator.commands.InitHamsterCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.Territory;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.tile.Grain;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.tile.Tile;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.tile.TileContent;
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
    private final PropertyMap<Hamster> propertyMap = new PropertyMap<>(this, currentTile, direction, grainInMouth);

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

    public Direction getDirection() {
        return direction.get();
    }

    public ReadOnlyListProperty<Grain> grainInMouthProperty() {
        return this.grainInMouth.getReadOnlyProperty();
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
    public AbstractBaseCommand<InitHamsterCommandSpecification> getInitializeHamsterCommand(final Territory territory, final Optional<Location> location,
            final Direction direction, final int grainCount) {
        return new InitHamsterCommand(this, territory, new InitHamsterCommandSpecification(location, direction, grainCount));
    }

    public AbstractBaseCommand<PropertyCommandSpecification> getRemoveGrainCommand(final Grain grain) {
        return UnidirectionalUpdatePropertyCommand.createPropertyUpdateCommand(this.propertyMap, "grainInMouth", grain, ActionKind.REMOVE);
    }

    public AbstractBaseCommand<PropertyCommandSpecification> getAddGrainCommand(final Grain grain) {
        return UnidirectionalUpdatePropertyCommand.createPropertyUpdateCommand(this.propertyMap, "grainInMouth", grain, ActionKind.ADD);
    }

    public AbstractBaseCommand<PropertyCommandSpecification> getSetDirectionCommand(final Direction direction) {
        return UnidirectionalUpdatePropertyCommand.createPropertyUpdateCommand(this.propertyMap, "direction", direction, ActionKind.SET);
    }

    public AbstractBaseCommand<PropertyCommandSpecification> getSetCurrentTileCommand(final Optional<Tile> newTile) {
        return UnidirectionalUpdatePropertyCommand.createPropertyUpdateCommand(this.propertyMap, "currentTile", newTile, ActionKind.SET);
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