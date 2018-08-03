package de.unistuttgart.iste.rss.oo.hamstersimulator.hamster;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import de.unistuttgart.iste.rss.oo.hamstersimulator.HamsterSimulator;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.PropertyMap;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Direction;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.LocationVector;
import de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.commands.InitHamsterCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.commands.PickGrainCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.commands.PutGrainCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.commands.TurnLeftCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.simulator.commands.MoveCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.Grain;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.Territory;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.Tile;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.TileContent;
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

    private final HamsterSimulator simulator;

    private final ReadOnlyObjectWrapper<Optional<Tile>> currentTile = new ReadOnlyObjectWrapper<>(this,"currentTile", Optional.empty());
    private final ReadOnlyObjectWrapper<Direction> direction = new ReadOnlyObjectWrapper<>(this, "direction", Direction.NORTH);
    private final ReadOnlyListWrapper<Grain> grainInMouth = new ReadOnlyListWrapper<>(this, "grainInMouth", FXCollections.observableArrayList());
    private final PropertyMap<Hamster> propertyMap = new PropertyMap<>(this, currentTile, direction, grainInMouth);

    /*
     * Constructors
     */
    // TODO: Fixme, make private by using a creational pattern
    public Hamster(final HamsterSimulator simulator, final Optional<Tile> initialTile, final Direction direction, final int grainInMouth) {
        super();

        checkNotNull(initialTile);
        checkNotNull(direction);
        checkArgument(grainInMouth >= 0, "Grain in mouth needs to be zero or greater.");

        this.simulator = simulator;
        this.direction.set(direction);
        this.currentTile.set(initialTile);
        hamsterSet.add(this);
        init(initialTile, direction, grainInMouth);
    }

    Hamster(final HamsterSimulator simulator, final Optional<Tile> initialTile, final Direction direction) {
        this(simulator, initialTile, direction, 0);
    }

    Hamster(final HamsterSimulator simulator, final Optional<Tile> initialTile) {
        this(simulator, initialTile, Direction.NORTH, 0);
    }

    public Hamster(final HamsterSimulator simulator) {
        this(simulator, Optional.empty(), Direction.NORTH, 0);
    }

    /*
     * Commands
     */
    public void init(final Optional<Tile> initialTile, final Direction direction, final int grainInMouth) {
        this.simulator.getCommandStack().execute(new InitHamsterCommand(propertyMap, initialTile, direction, grainInMouth));
    }

    public void move() {
        this.simulator.getCommandStack().execute(new MoveCommand(propertyMap));
    }

    public void turnLeft() {
        this.simulator.getCommandStack().execute(new TurnLeftCommand(propertyMap));
    }

    public void pickGrain() {
        this.simulator.getCommandStack().execute(new PickGrainCommand(propertyMap));
    }

    public void putGrain() {
        this.simulator.getCommandStack().execute(new PutGrainCommand(propertyMap));
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

    public ReadOnlyObjectProperty<Optional<Tile>> currentTileProperty() {
        return this.currentTile.getReadOnlyProperty();
    }

    public Optional<Tile> getCurrentTile() {
        return this.currentTile.get();
    }

    public ReadOnlyListProperty<Grain> grainInMouthProperty() {
        return this.grainInMouth.getReadOnlyProperty();
    }

    public List<Grain> getGrainInMouth() {
        return Collections.unmodifiableList(grainInMouth.get());
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