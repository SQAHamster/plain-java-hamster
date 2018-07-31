package de.unistuttgart.iste.rss.oo.hamstersimulator.hamster;

import java.util.Optional;

import de.unistuttgart.iste.rss.oo.hamstersimulator.HamsterSimulator;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.hamster.MoveCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.hamster.PickGrainCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.hamster.PutGrainCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.hamster.TurnLeftCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Direction;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.LocationVector;
import de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.events.HamsterStateListener;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.Grain;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.TileContent;

public class Hamster extends TileContent {

    private final HamsterSimulator simulator;
    private final HamsterState state;
    private final HamsterStateChanger stateChanger;

    /*
     * Constructors
     */
    // TODO: Fixme, make private by using a creational pattern
    public Hamster(final HamsterSimulator simulator, final Location initialPosition, final Direction direction, final int grainInMouth) {
        super();
        this.simulator = simulator;
        this.state = new HamsterState(this.simulator.getTerritory(), this);
        this.stateChanger = this.state.getStateChanger();
        init(initialPosition, direction, grainInMouth);
    }

    public Hamster(final HamsterSimulator simulator) {
        this(simulator,null,Direction.NORTH,0);
    }

    public void init(final Location initialPosition, final Direction direction, final int grainInMouth) {
        this.stateChanger.setDirection(direction);
        for (int i = 0; i < grainInMouth; i++) {
            this.stateChanger.addGrainToMouth(new Grain());
        }
        Optional.ofNullable(initialPosition).ifPresent(pos -> this.simulator.getTerritory().getTileAt(pos).addObjectToContent(Hamster.this));
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
        this.simulator.getCommandStack().execute(new MoveCommand(this.simulator.getTerritory(), stateChanger));
    }

    public void turnLeft() {
        this.simulator.getCommandStack().execute(new TurnLeftCommand(this.simulator.getTerritory(), stateChanger));
    }

    public void pickGrain() {
        this.simulator.getCommandStack().execute(new PickGrainCommand(this.simulator.getTerritory(), stateChanger));
    }

    public void putGrain() {
        this.simulator.getCommandStack().execute(new PutGrainCommand(this.simulator.getTerritory(), stateChanger));
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
        final Location potentialNewLocation = this.getCurrentPosition().orElseThrow(IllegalStateException::new).translate(movementVector);
        return this.simulator.getTerritory().getTileAt(potentialNewLocation).canEnter();
    }

    public boolean grainAvailable() {
        return this.state.getCurrentTile().orElseThrow(IllegalStateException::new).countObjectsOfType(Grain.class) > 0;
    }

    public boolean mouthEmpty() {
        return state.getGrainInMouth().isEmpty();
    }

    public Optional<Location> getCurrentPosition() {
        return this.state.getCurrentTile().map(t -> t.getTileLocation());
    }

    public Direction getDirection() {
        return this.state.getDirection();
    }

    public void reset() {
        this.state.reset();
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