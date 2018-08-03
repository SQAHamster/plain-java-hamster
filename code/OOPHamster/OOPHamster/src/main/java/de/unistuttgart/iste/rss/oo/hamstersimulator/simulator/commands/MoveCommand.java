package de.unistuttgart.iste.rss.oo.hamstersimulator.simulator.commands;

import java.util.Optional;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.PropertyMap;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.LocationVector;
import de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.Hamster;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.tile.Tile;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.tile.commands.SetCurrentTileCommand;

public class MoveCommand extends HamsterCompositeBaseCommand {

    public MoveCommand(final PropertyMap<Hamster> hamsterState) {
        super(hamsterState);
    }

    @Override
    protected void buildBeforeFirstExecution(final CompositeCommandBuilder builder) {
        assert this.hamster.getCurrentTile().isPresent();
        final LocationVector movementVector = hamster.getDirection().getMovementVector();
        final Location newHamsterPosition = this.hamster.getCurrentTile().get().getLocation().translate(movementVector);

        assert this.hamster.getCurrentTerritory().isLocationInTerritory(newHamsterPosition);
        final Tile newTile = this.hamster.getCurrentTerritory().getTileAt(newHamsterPosition);

        builder.add(new SetCurrentTileCommand<Hamster>(this.hamsterState, Optional.of(newTile)));
    }

}
