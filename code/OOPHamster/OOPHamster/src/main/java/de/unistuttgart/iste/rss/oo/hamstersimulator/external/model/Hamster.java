package de.unistuttgart.iste.rss.oo.hamstersimulator.external.model;

import static de.unistuttgart.iste.rss.oo.hamstersimulator.util.Preconditions.checkArgument;
import static de.unistuttgart.iste.rss.oo.hamstersimulator.util.Preconditions.checkNotNull;

import java.util.Optional;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.Command;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.GameCommandStack;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Direction;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.LocationVector;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.InitHamsterCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.MoveCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.PickGrainCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.PutGrainCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.Tile;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.TurnLeftCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.command.specification.InitHamsterCommandSpecification;

public class Hamster {

    private final de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.Hamster internalHamster;
    private GameCommandStack<Command> commandStack;

    public Hamster() {
        super();
        this.internalHamster = new de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.Hamster();
    }

    public Hamster(final Territory territory, final Location location, final Direction newDirection, final int newGrainCount) {
        this();
        init(territory, location, newDirection, newGrainCount);
    }

    private Hamster(final Territory territory) {
        super();
        this.internalHamster = territory.getInternalTerritory().getDefaultHamster();
        this.commandStack = territory.getGame().getCommandStack();
    }

    /*
     * Commands
     */
    public void init(final Territory territory, final Location location, final Direction newDirection, final int newGrainCount) {
        checkNotNull(territory);
        checkNotNull(location);
        checkNotNull(newDirection);
        checkArgument(newGrainCount >= 0);
        this.commandStack = territory.getGame().getCommandStack();

        commandStack.execute(new InitHamsterCommand(this.internalHamster, territory.getInternalTerritory(), new InitHamsterCommandSpecification(Optional.of(location), newDirection, newGrainCount)));
    }

    public void move() {
        commandStack.execute(new MoveCommand(this.internalHamster));
    }

    public void turnLeft() {
        commandStack.execute(new TurnLeftCommand(this.internalHamster));
    }

    public void pickGrain() {
        commandStack.execute(new PickGrainCommand(this.internalHamster));
    }

    public void putGrain() {
        commandStack.execute(new PutGrainCommand(this.internalHamster));
    }

    public void readNumber() {
        // TODO - implement Hamster.readNumber

    }

    public void readString() {
        // TODO - implement Hamster.readString

    }

    public void write(final String text) {
        // TODO - implement Hamster.write
        System.out.println(text);
    }

    /*
     * Queries
     */
    public boolean frontIsClear() {
        final LocationVector movementVector = this.internalHamster.getDirection().getMovementVector();
        final Tile currentTile = this.internalHamster.getCurrentTile().orElseThrow(IllegalArgumentException::new);
        final Location potentialNewLocation = currentTile.getLocation().translate(movementVector);

        if (!currentTile.getTerritory().isLocationInTerritory(potentialNewLocation)) {
            return false;
        }

        return !currentTile.getTerritory().getTileAt(potentialNewLocation).isBlocked();
    }

    public boolean grainAvailable() {
        return this.internalHamster.getCurrentTile().orElseThrow(IllegalStateException::new).getGrainCount() > 0;
    }

    public boolean mouthEmpty() {
        return this.internalHamster.getGrainInMouth().isEmpty();
    }

    public static Hamster fromInternalDefaultHamster(final Territory territory) {
        return new Hamster(territory);
    }
}
