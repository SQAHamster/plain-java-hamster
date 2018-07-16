package de.unistuttgart.iste.rss.oo.hamster;

import java.util.LinkedList;
import java.util.List;

import de.unistuttgart.iste.rss.oo.hamster.commands.MoveCommand;
import de.unistuttgart.iste.rss.oo.hamster.commands.PickGrainCommand;
import de.unistuttgart.iste.rss.oo.hamster.commands.PutGrainCommand;
import de.unistuttgart.iste.rss.oo.hamster.commands.TurnLeftCommand;

public class Hamster extends TileContent {

    private final HamsterSimulator simulator;
    private final CommandStack commandStack;
    private final Territory territory;
    private final Location initialPosition;
    private final int id;

    private Tile currentTile;
    private Direction direction = Direction.NORTH;
    private final List<Grain> grainInMouth = new LinkedList<>();

    /*
     * Constructors
     */
    public Hamster(final HamsterSimulator simulator, final int id, final Location initialPosition) {
        super();
        this.simulator = simulator;
        this.commandStack = simulator.getCommandStack();
        this.initialPosition = initialPosition;
        this.id = id;
        this.territory = this.simulator.getTerritory();
        this.currentTile = this.territory.getTileAt(this.initialPosition);
    }

    public Hamster(final HamsterSimulator simulator, final int id, final Location initialPosition, final Direction direction) {
        this(simulator, id, initialPosition);
        this.direction = direction;
    }

    public Hamster(final HamsterSimulator simulator, final int id, final Location initialPosition, final Direction direction, final int grainInMouth) {
        this(simulator, id, initialPosition, direction);
        for (int i = 0; i < grainInMouth; i++) {
            this.grainInMouth.add(new Grain());
        }
    }

    /*
     * Commands
     */
    public void move() {
        this.commandStack.execute(new MoveCommand(manipulator, territory));
    }

    public void turnLeft() {
        this.commandStack.execute(new TurnLeftCommand(manipulator, territory));
    }

    public void pickGrain() {
        this.commandStack.execute(new PickGrainCommand(manipulator, territory));
    }

    public void putGrain() {
        this.commandStack.execute(new PutGrainCommand(manipulator, territory));
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
        final LocationVector movementVector = this.direction.getMovementVector();
        final Location potentialNewLocation = this.getCurrentPosition().translate(movementVector);
        return territory.getTileAt(potentialNewLocation).canEnter();
    }

    public boolean grainAvailable() {
        return this.currentTile.countObjectsOfType(Grain.class) > 0;
    }

    public boolean mouthEmpty() {
        return grainInMouth.isEmpty();
    }

    public Location getCurrentPosition() {
        return this.currentTile.getTileLocation();
    }

    public Location getInitialPosition() {
        return this.initialPosition;
    }

    public Direction getDirection() {
        return this.direction;
    }

    /*
     * Implementation Internals... not part of the API
     */
    private final HamsterManipulator manipulator = new HamsterManipulator();

    public class HamsterManipulator {

        public void setLocation(final Location newLocation) {
            Hamster.this.currentTile.removeObjectFromContent(Hamster.this);
            Hamster.this.currentTile = Hamster.this.territory.getTileAt(newLocation);
            Hamster.this.currentTile.addObjectToContent(Hamster.this);
        }

        public void setDirection(final Direction newDirection) {
            Hamster.this.direction = newDirection;
        }

        public Hamster getHamster() {
            return Hamster.this;
        }

        public void addGrain(final Grain pickedGrain) {
            Hamster.this.grainInMouth.add(pickedGrain);
        }

        public Grain removeAnyGrain() {
            final Grain result = Hamster.this.grainInMouth.get(0);
            Hamster.this.grainInMouth.remove(0);
            return result;
        }

    }

    @Override
    protected boolean blocksEntrance() {
        return false;
    }

}