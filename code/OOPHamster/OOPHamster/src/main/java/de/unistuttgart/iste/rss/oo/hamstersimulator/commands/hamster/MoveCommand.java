package de.unistuttgart.iste.rss.oo.hamstersimulator.commands.hamster;

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
        this.previousHamsterTile = this.territory.getTileAt(this.hamster.getCurrentPosition().get());

        assert this.territory.isLocationInTerritory(newHamsterPosition);
        final Tile newTile = territory.getTileAt(newHamsterPosition);

        this.previousHamsterTile.removeObjectFromContent(this.hamster);
        newTile.addObjectToContent(this.hamster);
    }

    @Override
    public void undo() {
        assert this.hamster.getCurrentPosition().isPresent();

        this.territory.getTileAt(this.hamster.getCurrentPosition().get()).removeObjectFromContent(this.hamster);
        this.previousHamsterTile.addObjectToContent(this.hamster);
    }

}
