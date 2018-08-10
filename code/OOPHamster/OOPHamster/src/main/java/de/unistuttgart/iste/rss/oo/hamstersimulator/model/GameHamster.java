package de.unistuttgart.iste.rss.oo.hamstersimulator.model;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Optional;

import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Direction;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.LocationVector;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.Grain;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.Hamster;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.InitHamsterCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.InitHamsterCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.MoveCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.PickGrainCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.PutGrainCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.Territory;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.Tile;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.TurnLeftCommand;

public class GameHamster {

    private final Hamster hamster;

    public GameHamster() {
        super();
        this.hamster = new Hamster();
    }



    public GameHamster(final Territory territory, final Location location, final Direction newDirection, final int newGrainCount) {
        this();
        init(territory, location, newDirection, newGrainCount);
    }
    /*
     * Commands
     */

    private GameHamster(final Hamster hamster) {
        super();
        this.hamster = hamster;
    }

    public void init(final Territory territory, final Location location, final Direction newDirection, final int newGrainCount) {
        checkNotNull(territory);
        checkNotNull(location);
        checkNotNull(newDirection);
        checkArgument(newGrainCount >= 0);

        territory.getCommandStack().execute(new InitHamsterCommand(this.hamster, territory, new InitHamsterCommandSpecification(Optional.of(location), newDirection, newGrainCount)));
    }

    public void move() {
        this.hamster.getCurrentTerritory().getCommandStack().execute(new MoveCommand(this.hamster));
    }

    public void turnLeft() {
        this.hamster.getCurrentTerritory().getCommandStack().execute(new TurnLeftCommand(this.hamster));
    }

    public void pickGrain() {
        this.hamster.getCurrentTerritory().getCommandStack().execute(new PickGrainCommand(this.hamster));
    }

    public void putGrain() {
        this.hamster.getCurrentTerritory().getCommandStack().execute(new PutGrainCommand(this.hamster));
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
        final LocationVector movementVector = this.hamster.getDirection().getMovementVector();
        final Location potentialNewLocation = this.hamster.getCurrentTile().orElseThrow(IllegalStateException::new).getLocation().translate(movementVector);
        final Tile currentTile = this.hamster.getCurrentTile().orElseThrow(IllegalArgumentException::new);

        if (!currentTile.getTerritory().isLocationInTerritory(potentialNewLocation)) {
            return false;
        }

        return currentTile.getTerritory().getTileAt(potentialNewLocation).canEnter();
    }

    public boolean grainAvailable() {
        return this.hamster.getCurrentTile().orElseThrow(IllegalStateException::new).countObjectsOfType(Grain.class) > 0;
    }

    public boolean mouthEmpty() {
        return this.hamster.getGrainInMouth().isEmpty();
    }

    public static GameHamster fromInternalHamster(final Hamster hamster) {
        return new GameHamster(hamster);
    }
}
