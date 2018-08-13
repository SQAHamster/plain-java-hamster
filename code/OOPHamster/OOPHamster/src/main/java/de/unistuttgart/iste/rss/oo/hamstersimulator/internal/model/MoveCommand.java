package de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model;

import java.util.Optional;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.UnidirectionalUpdatePropertyCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.specification.UnidirectionalUpdatePropertyCommandSpecification.ActionKind;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.LocationVector;

public class MoveCommand extends AbstractHamsterCompositeBaseCommand {

    public MoveCommand(final Hamster hamster) {
        super(hamster);
    }

    @Override
    protected void buildBeforeFirstExecution(final CompositeCommandBuilder builder) {
        assert this.hamster.getCurrentTile().isPresent();

        final LocationVector movementVector = hamster.getDirection().getMovementVector();
        final Location newHamsterPosition = this.hamster.getCurrentTile().get().getLocation().translate(movementVector);

        assert this.hamster.getCurrentTerritory().isLocationInTerritory(newHamsterPosition);
        final Tile newTile = this.hamster.getCurrentTerritory().getTileAt(newHamsterPosition);

        builder.add(UnidirectionalUpdatePropertyCommand.createPropertyUpdateCommand(this.hamster.getCurrentTile().get().content, this.hamster, ActionKind.REMOVE));
        builder.add(UnidirectionalUpdatePropertyCommand.createPropertyUpdateCommand(this.hamster.currentTile, Optional.of(newTile), ActionKind.SET));
        builder.add(UnidirectionalUpdatePropertyCommand.createPropertyUpdateCommand(newTile.content, this.hamster, ActionKind.ADD));
    }

}
