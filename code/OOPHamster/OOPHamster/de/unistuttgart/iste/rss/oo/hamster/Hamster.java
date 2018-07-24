package de.unistuttgart.iste.rss.oo.hamster;

import java.util.LinkedList;

import de.unistuttgart.iste.rss.oo.hamster.commands.MoveCommand;
import de.unistuttgart.iste.rss.oo.hamster.commands.PickGrainCommand;
import de.unistuttgart.iste.rss.oo.hamster.commands.PutGrainCommand;
import de.unistuttgart.iste.rss.oo.hamster.commands.TurnLeftCommand;
import de.unistuttgart.iste.rss.oo.hamster.datatypes.Direction;
import de.unistuttgart.iste.rss.oo.hamster.datatypes.Location;
import de.unistuttgart.iste.rss.oo.hamster.datatypes.LocationVector;
import de.unistuttgart.iste.rss.oo.hamster.state.HamsterManipulator;
import de.unistuttgart.iste.rss.oo.hamster.state.HamsterState;
import de.unistuttgart.iste.rss.oo.hamster.state.HamsterStateListener;

public class Hamster extends TileContent {

    private static Hamster defaultHamster = null;

    private final HamsterSimulator simulator;
    private final CommandStack commandStack;

    private final Location initialPosition;

    private final HamsterState state;
    private final HamsterManipulator manipulator;

    /*
     * Constructors
     */
    Hamster(final HamsterSimulator simulator, final Location initialPosition, final Direction direction, final int grainInMouth) {
        super();
        this.simulator = simulator;
        this.commandStack = simulator.getCommandStack();
        this.initialPosition = initialPosition;
        final LinkedList<Grain> myGrain = new LinkedList<>();
        for (int i = 0; i < grainInMouth; i++) {
            myGrain.add(new Grain());
        }
        this.state = new HamsterState(this, simulator.getTerritory().getTileAt(this.initialPosition), direction, myGrain);
        this.manipulator = new HamsterManipulator(this, state, simulator.getTerritory());
    }

    Hamster(final HamsterSimulator simulator, final Location initialPosition, final Direction direction) {
        this(simulator, initialPosition, direction, 0);
    }

    Hamster(final HamsterSimulator simulator, final Location initialPosition) {
        this(simulator, initialPosition, Direction.NORTH, 0);
    }

    /*
     * Commands
     */
    public void move() {
        this.commandStack.execute(new MoveCommand(manipulator));
    }

    public void turnLeft() {
        this.commandStack.execute(new TurnLeftCommand(manipulator));
    }

    public void pickGrain() {
        this.commandStack.execute(new PickGrainCommand(manipulator));
    }

    public void putGrain() {
        this.commandStack.execute(new PutGrainCommand(manipulator));
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
        final LocationVector movementVector = this.state.getDirection().getMovementVector();
        final Location potentialNewLocation = this.getCurrentPosition().translate(movementVector);
        return manipulator.getTerritory().getTileAt(potentialNewLocation).canEnter();
    }

    public boolean grainAvailable() {
        return this.state.getCurrentTile().countObjectsOfType(Grain.class) > 0;
    }

    public boolean mouthEmpty() {
        return state.getGrainInMouth().isEmpty();
    }

    public Location getCurrentPosition() {
        return this.state.getCurrentTile().getTileLocation();
    }

    public Location getInitialPosition() {
        return this.initialPosition;
    }

    public Direction getDirection() {
        return this.state.getDirection();
    }

    public void addHamsterStateListener(final HamsterStateListener listener) {
        state.addHamsterStateListener(listener);
    }

    public void removeHamsterStateListener(final HamsterStateListener listener) {
        state.removeHamsterStateListener(listener);
    }

    @Override
    protected boolean blocksEntrance() {
        return false;
    }

    public static Hamster getDefaultHamster(final HamsterSimulator simulator) {
        if (defaultHamster == null) {
            defaultHamster = new Hamster(simulator, new Location(0,0));
        }
        return defaultHamster;
    }
}