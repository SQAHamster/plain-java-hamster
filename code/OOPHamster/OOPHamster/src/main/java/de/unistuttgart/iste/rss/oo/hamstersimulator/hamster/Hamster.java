package de.unistuttgart.iste.rss.oo.hamstersimulator.hamster;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.Command;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.CommandStack;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.PropertyCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.PropertyCommandSpecification.ActionKind;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.PropertyMap;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.UnidirectionalUpdatePropertyCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Direction;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.LocationVector;
import de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.commands.InitHamsterCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.commands.InitHamsterCommandParameter;
import de.unistuttgart.iste.rss.oo.hamstersimulator.simulator.commands.MoveCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.simulator.commands.PickGrainCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.simulator.commands.PutGrainCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.simulator.commands.TurnLeftCommand;
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
     * Commands
     */

    public void move(final CommandStack stack) {
        stack.execute(new MoveCommand(this));
    }

    public void turnLeft(final CommandStack stack) {
        stack.execute(new TurnLeftCommand(this));
    }

    public void pickGrain(final CommandStack stack) {
        stack.execute(new PickGrainCommand(this));
    }

    public void putGrain(final CommandStack stack) {
        stack.execute(new PutGrainCommand(this));
    }

    public void readNumber() {
        // TODO - implement Hamster.readNumber
        throw new UnsupportedOperationException();
    }

    public void readString() {
        // TODO - implement Hamster.readString
        throw new UnsupportedOperationException();
    }

    public void write() {
        // TODO - implement Hamster.write
        throw new UnsupportedOperationException();
    }

    /*
     * Queries
     */
    public boolean frontIsClear() {
        final LocationVector movementVector = this.getDirection().getMovementVector();
        final Location potentialNewLocation = this.getCurrentTile().orElseThrow(IllegalStateException::new).getLocation().translate(movementVector);
        final Tile currentTile = this.getCurrentTile().orElseThrow(IllegalArgumentException::new);

        if (!currentTile.getTerritory().isLocationInTerritory(potentialNewLocation)) {
            return false;
        }

        return currentTile.getTerritory().getTileAt(potentialNewLocation).canEnter();
    }

    public boolean grainAvailable() {
        return this.getCurrentTile().orElseThrow(IllegalStateException::new).countObjectsOfType(Grain.class) > 0;
    }

    public boolean mouthEmpty() {
        return this.getGrainInMouth().isEmpty();
    }

    public Territory getCurrentTerritory() {
        return this.getCurrentTile().orElseThrow(IllegalStateException::new).getTerritory();
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

    /*
     * Commands
     */

    public Command getInitializeHamsterCommand(final Optional<Territory> territory, final Optional<Location> location, final Direction newDirection, final int newGrainCount) {
        return new InitHamsterCommand(this, territory, new InitHamsterCommandParameter(location, newDirection, newGrainCount));
    }

    public Command getRemoveGrainCommand(final Grain grain) {
        return UnidirectionalUpdatePropertyCommand.createPropertyUpdateCommand(this.propertyMap, "grainInMouth", grain, ActionKind.REMOVE);
    }

    public Command getAddGrainCommand(final Grain grain) {
        return UnidirectionalUpdatePropertyCommand.createPropertyUpdateCommand(this.propertyMap, "grainInMouth", grain, ActionKind.ADD);
    }

    public Command getSetDirectionCommand(final Direction direction) {
        return UnidirectionalUpdatePropertyCommand.createPropertyUpdateCommand(this.propertyMap, "direction", direction, ActionKind.SET);
    }

    public Command getSetCurrentTileCommand(final Optional<Tile> newTile) {
        return UnidirectionalUpdatePropertyCommand.createPropertyUpdateCommand(this.propertyMap, "currentTile", newTile, ActionKind.SET);
    }

    public Command getCommandFromSpecification(final PropertyCommandSpecification spec) {
        return new UnidirectionalUpdatePropertyCommand<Hamster>(this.propertyMap, spec);
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