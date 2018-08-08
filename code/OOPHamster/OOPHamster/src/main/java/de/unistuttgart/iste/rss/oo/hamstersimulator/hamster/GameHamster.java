package de.unistuttgart.iste.rss.oo.hamstersimulator.hamster;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.CommandStack;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.LocationVector;
import de.unistuttgart.iste.rss.oo.hamstersimulator.simulator.commands.GameCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.simulator.commands.MoveCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.simulator.commands.PickGrainCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.simulator.commands.PutGrainCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.simulator.commands.TurnLeftCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.Territory;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.tile.Grain;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.tile.Tile;

public class GameHamster extends Hamster {

    /*
     * Commands
     */

    public void move(final CommandStack<GameCommand> stack) {
        stack.execute(new MoveCommand(this));
    }

    public void turnLeft(final CommandStack<GameCommand> stack) {
        stack.execute(new TurnLeftCommand(this));
    }

    public void pickGrain(final CommandStack<GameCommand> stack) {
        stack.execute(new PickGrainCommand(this));
    }

    public void putGrain(final CommandStack<GameCommand> stack) {
        stack.execute(new PutGrainCommand(this));
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
