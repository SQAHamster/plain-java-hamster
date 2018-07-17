package de.unistuttgart.iste.rss.oo.hamster;

import java.util.LinkedList;

import de.unistuttgart.iste.rss.oo.hamster.commands.MoveCommand;
import de.unistuttgart.iste.rss.oo.hamster.commands.PickGrainCommand;
import de.unistuttgart.iste.rss.oo.hamster.commands.PutGrainCommand;
import de.unistuttgart.iste.rss.oo.hamster.commands.TurnLeftCommand;
import de.unistuttgart.iste.rss.oo.hamster.state.HamsterManipulator;
import de.unistuttgart.iste.rss.oo.hamster.state.HamsterState;

public class Hamster extends TileContent {

    private final HamsterSimulator simulator;
    private final CommandStack commandStack;
    private final Territory territory;
    private final Location initialPosition;
    private final HamsterManipulator manipulator;
    private final int id;

    private final HamsterState state;

    /*
     * Constructors
     */
    public Hamster(final HamsterSimulator simulator, final int id, final Location initialPosition) {
        this(simulator, id, initialPosition, Direction.NORTH, 0);
    }

    public Hamster(final HamsterSimulator simulator, final int id, final Location initialPosition, final Direction direction) {
        this(simulator, id, initialPosition, direction, 0);
    }

    public Hamster(final HamsterSimulator simulator, final int id, final Location initialPosition, final Direction direction, final int grainInMouth) {
        super();
        this.simulator = simulator;
        this.territory = simulator.getTerritory();
        this.commandStack = simulator.getCommandStack();
        this.initialPosition = initialPosition;
        this.id = id;
        final LinkedList<Grain> myGrain = new LinkedList<>();
        for (int i = 0; i < grainInMouth; i++) {
            myGrain.add(new Grain());
        }
        this.state = new HamsterState(this.territory.getTileAt(this.initialPosition), direction, myGrain);
        this.manipulator = new HamsterManipulator(this, state, territory);
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
        return territory.getTileAt(potentialNewLocation).canEnter();
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

    @Override
    protected boolean blocksEntrance() {
        return false;
    }

}