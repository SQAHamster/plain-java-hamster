package de.unistuttgart.iste.rss.oo.hamstersimulator.hamster;

import java.util.LinkedList;

import de.unistuttgart.iste.rss.oo.hamstersimulator.CommandStack;
import de.unistuttgart.iste.rss.oo.hamstersimulator.HamsterSimulator;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.MoveCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.PickGrainCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.PutGrainCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.TurnLeftCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Direction;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.LocationVector;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.Grain;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.TileContent;

public class Hamster extends TileContent {

    private final CommandStack commandStack;
    private final Location initialPosition;

    private final HamsterState state;
    private final HamsterManipulator manipulator;

    /*
     * Constructors
     */
    // TODO: Fixme, make private by using a creational pattern
    public Hamster(final HamsterSimulator simulator, final Location initialPosition, final Direction direction, final int grainInMouth) {
        super();
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

    /*
     * OO-Design Methods
     */
    public void addHamsterStateListener(final HamsterStateListener listener) {
        state.addHamsterStateListener(listener);
    }

    public void removeHamsterStateListener(final HamsterStateListener listener) {
        state.removeHamsterStateListener(listener);
    }

    @Override
    public String toString() {
        return "Hamster";
    }

    @Override
    protected boolean blocksEntrance() {
        return false;
    }
}