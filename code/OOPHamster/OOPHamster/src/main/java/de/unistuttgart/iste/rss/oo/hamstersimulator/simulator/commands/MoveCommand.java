package de.unistuttgart.iste.rss.oo.hamstersimulator.simulator.commands;

import java.util.Optional;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.CompositeBaseCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.PropertyMap;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.LocationVector;
import de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.Hamster;
import de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.commands.SetCurrentTileCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.Tile;

public class MoveCommand extends CompositeBaseCommand {

    private final Hamster hamster;
    private final PropertyMap<Hamster> hamsterState;

    public MoveCommand(final PropertyMap<Hamster> hamsterState) {
        super();
        this.hamsterState = hamsterState;
        this.hamster = hamsterState.getPropertyOwner();
    }

    @Override
    protected void buildBeforeFirstExecution(final CompositeCommandBuilder builder) {
        assert this.hamster.getCurrentTile().isPresent();
        final LocationVector movementVector = hamster.getDirection().getMovementVector();
        final Location newHamsterPosition = this.hamster.getCurrentTile().get().getLocation().translate(movementVector);

        assert this.hamster.getCurrentTerritory().isLocationInTerritory(newHamsterPosition);
        final Tile newTile = this.hamster.getCurrentTerritory().getTileAt(newHamsterPosition);

        builder.add(new SetCurrentTileCommand(this.hamsterState, Optional.of(newTile)));
    }

}
