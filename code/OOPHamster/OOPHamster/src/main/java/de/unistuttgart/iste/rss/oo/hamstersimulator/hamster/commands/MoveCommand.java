package de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.commands;

import java.util.Optional;

import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.LocationVector;
import de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.Hamster.HamsterStateChanger;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.Tile;

public class MoveCommand extends HamsterCommand {

    private Tile previousHamsterTile;

    public MoveCommand(final HamsterStateChanger stateChanger) {
        super(stateChanger);
    }

    @Override
    public void execute() {
        assert this.hamster.getCurrentTile().isPresent();

        final LocationVector movementVector = hamster.getDirection().getMovementVector();
        final Location newHamsterPosition = this.hamster.getCurrentTile().get().getLocation().translate(movementVector);
        this.previousHamsterTile = this.getTerritory().getTileAt(this.hamster.getCurrentTile().get().getLocation());

        assert this.getTerritory().isLocationInTerritory(newHamsterPosition);
        final Tile newTile = getTerritory().getTileAt(newHamsterPosition);

        this.stateChanger.setCurrentTile(Optional.of(newTile));
    }

    @Override
    public void undo() {
        assert this.hamster.getCurrentTile().isPresent();

        this.stateChanger.setCurrentTile(Optional.of(this.previousHamsterTile));
    }

}
