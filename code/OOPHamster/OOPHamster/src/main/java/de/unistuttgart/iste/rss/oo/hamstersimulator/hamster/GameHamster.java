package de.unistuttgart.iste.rss.oo.hamstersimulator.hamster;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Optional;

import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Direction;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.LocationVector;
import de.unistuttgart.iste.rss.oo.hamstersimulator.simulator.commands.InitHamsterCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.simulator.commands.InitHamsterCommandParameter;
import de.unistuttgart.iste.rss.oo.hamstersimulator.simulator.commands.MoveCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.simulator.commands.PickGrainCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.simulator.commands.PutGrainCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.simulator.commands.TurnLeftCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.Territory;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.tile.Grain;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.tile.Tile;

public class GameHamster extends Hamster {

    public GameHamster() {}

    public GameHamster(final Territory territory, final Location location, final Direction newDirection, final int newGrainCount) {
        super();
        init(territory, location, newDirection, newGrainCount);
    }
    /*
     * Commands
     */

    public void init(final Territory territory, final Location location, final Direction newDirection, final int newGrainCount) {
        checkNotNull(territory);
        checkNotNull(location);
        checkNotNull(newDirection);
        checkArgument(newGrainCount >= 0);

        territory.getCommandStack().execute(new InitHamsterCommand(this, territory, new InitHamsterCommandParameter(Optional.of(location), newDirection, newGrainCount)));
    }

    public void move() {
        this.getCurrentTerritory().getCommandStack().execute(new MoveCommand(this));
    }

    public void turnLeft() {
        this.getCurrentTerritory().getCommandStack().execute(new TurnLeftCommand(this));
    }

    public void pickGrain() {
        this.getCurrentTerritory().getCommandStack().execute(new PickGrainCommand(this));
    }

    public void putGrain() {
        this.getCurrentTerritory().getCommandStack().execute(new PutGrainCommand(this));
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

}
