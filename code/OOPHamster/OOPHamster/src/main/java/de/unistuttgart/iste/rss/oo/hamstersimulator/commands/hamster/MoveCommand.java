package de.unistuttgart.iste.rss.oo.hamstersimulator.commands.hamster;

import java.util.Optional;

import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.LocationVector;
import de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.HamsterStateChanger;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.Territory;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.Tile;

public class MoveCommand extends HamsterCommand {

    private Tile previousHamsterTile;

    public MoveCommand(final Territory territory, final HamsterStateChanger stateChanger) {
        super(territory, stateChanger);
    }

    @Override
    public void execute() {
        assert this.hamster.getCurrentPosition().isPresent();

        final LocationVector movementVector = hamster.getDirection().getMovementVector();
        final Location newHamsterPosition = this.hamster.getCurrentPosition().get().translate(movementVector);
        this.previousHamsterTile = this.territory.getTileAt(newHamsterPosition);

        assert this.territory.isLocationInTerritory(newHamsterPosition);

        final Tile newTile = territory.getTileAt(newHamsterPosition);
        this.stateChanger.setCurrentTile(Optional.of(newTile));
    }

    @Override
    public void undo() {
        this.stateChanger.setCurrentTile(Optional.of(previousHamsterTile));
    }

}
