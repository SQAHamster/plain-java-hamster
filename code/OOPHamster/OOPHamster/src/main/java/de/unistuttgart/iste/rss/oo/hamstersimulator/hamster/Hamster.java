package de.unistuttgart.iste.rss.oo.hamstersimulator.hamster;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import de.unistuttgart.iste.rss.oo.hamstersimulator.HamsterSimulator;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Direction;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.LocationVector;
import de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.commands.InitHamsterCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.commands.MoveCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.commands.PickGrainCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.commands.PutGrainCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.commands.TurnLeftCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.events.HamsterCreatedEvent;
import de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.events.HamsterCreatedListener;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.Grain;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.Territory;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.Tile;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.TileContent;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;

public class Hamster extends TileContent {

    private static final List<HamsterCreatedListener> creationListener = new LinkedList<>();

    private final HamsterSimulator simulator;

    private final ReadOnlyObjectWrapper<Optional<Tile>> currentTile = new ReadOnlyObjectWrapper<>();
    private final ReadOnlyObjectWrapper<Direction> direction = new ReadOnlyObjectWrapper<>();
    private final ReadOnlyListWrapper<Grain> grainInMouth = new ReadOnlyListWrapper<>();

    private final HamsterStateChanger hamsterStateAccess = new HamsterStateChanger();

    /*
     * Constructors
     */
    // TODO: Fixme, make private by using a creational pattern
    public Hamster(final HamsterSimulator simulator, final Optional<Tile> initialTile, final Direction direction, final int grainInMouth) {
        super();

        assert initialTile != null;

        this.simulator = simulator;
        this.grainInMouth.set(FXCollections.observableArrayList());
        this.direction.set(Direction.NORTH);
        this.currentTile.set(Optional.empty());
        notifyHamsterCreated(this);
        init(initialTile, direction, grainInMouth);
    }

    Hamster(final HamsterSimulator simulator, final Optional<Tile> initialTile, final Direction direction) {
        this(simulator, initialTile, direction, 0);
    }

    Hamster(final HamsterSimulator simulator, final Optional<Tile> initialTile) {
        this(simulator, initialTile, Direction.NORTH, 0);
    }

    public Hamster(final HamsterSimulator simulator) {
        this(simulator,Optional.empty(),Direction.NORTH,0);
    }

    public void init(final Optional<Tile> initialTile, final Direction direction, final int grainInMouth) {
        this.simulator.getCommandStack().execute(new InitHamsterCommand(hamsterStateAccess, initialTile, direction, grainInMouth));
    }

    /*
     * Commands
     */
    public void move() {
        this.simulator.getCommandStack().execute(new MoveCommand(hamsterStateAccess));
    }

    public void turnLeft() {
        this.simulator.getCommandStack().execute(new TurnLeftCommand(hamsterStateAccess));
    }

    public void pickGrain() {
        this.simulator.getCommandStack().execute(new PickGrainCommand(hamsterStateAccess));
    }

    public void putGrain() {
        this.simulator.getCommandStack().execute(new PutGrainCommand(hamsterStateAccess));
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

    public Optional<Tile> getCurrentTile() {
        return this.currentTile.get();
    }

    public Territory getCurrentTerritory() {
        return this.getCurrentTile().orElseThrow(IllegalStateException::new).getTerritory();
    }

    public Direction getDirection() {
        return direction.get();
    }

    public ReadOnlyObjectProperty<Direction> directionProperty() {
        return this.direction.getReadOnlyProperty();
    }

    public ReadOnlyObjectProperty<Optional<Tile>> currentTileProperty() {
        return this.currentTile.getReadOnlyProperty();
    }

    public ReadOnlyListProperty<Grain> grainInMouthProperty() {
        return this.grainInMouth.getReadOnlyProperty();
    }

    public List<Grain> getGrainInMouth() {
        return Collections.unmodifiableList(grainInMouth.get());
    }

    /*
     * State changers
     */
    public class HamsterStateChanger {

        public Hamster getHamster() {
            return Hamster.this;
        }

        public void setDirection(final Direction direction) {
            assert direction != null;

            //final Direction oldDirection = Hamster.this.direction;
            //Hamster.this.direction = direction;
            //fireStateChangedEvent(new HamsterChangedDirectionEvent(Hamster.this, oldDirection, Hamster.this.direction));
            Hamster.this.direction.set(direction);
        }

        public void addGrainToMouth(final Grain newGrain) {
            assert !Hamster.this.getGrainInMouth().contains(newGrain);

            Hamster.this.grainInMouth.add(newGrain);
        }

        public void removeGrainFromMouth(final Grain grainToRemove) {
            assert Hamster.this.grainInMouth.contains(grainToRemove);

            Hamster.this.grainInMouth.remove(grainToRemove);
        }

        public Grain getAnyGrain() {
            assert Hamster.this.grainInMouth.size() > 0;
            return Hamster.this.grainInMouth.get(0);
        }

        public void setCurrentTile(final Optional<Tile> newTile) {
            assert newTile != null;
            assert !Hamster.this.currentTile.get().isPresent() || Hamster.this.currentTile.get().get().hasObjectInContent(Hamster.this);
            assert !newTile.isPresent() || newTile.get().canEnter();

            Hamster.this.currentTile.set(newTile);
        }
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

    public static void addHamsterCreatedListener(final HamsterCreatedListener listener) {
        creationListener.add(listener);
    }

    public static void removeHamsterCreatedListener(final HamsterCreatedListener listener) {
        creationListener.remove(listener);
    }

    private static void notifyHamsterCreated(final Hamster hamster) {
        for (final HamsterCreatedListener listener : creationListener) {
            listener.hamsterCreated(new HamsterCreatedEvent(hamster));
        }
    }

}