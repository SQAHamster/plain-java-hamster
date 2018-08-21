package de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.territory;

import java.util.Optional;
import java.util.function.Function;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.CompositeCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.Command;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.CommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.LocationVector;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.hamster.GameHamster;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.hamster.ReadOnlyHamster;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.hamster.command.specification.InitHamsterCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.hamster.command.specification.MoveCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.hamster.command.specification.PickGrainCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.hamster.command.specification.PutGrainCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.util.LambdaVisitor;

public class GameTerritory extends EditorTerritory {

    private final Function<CommandSpecification, Command> editCommandFactory;

    public GameTerritory() {
        super();
        editCommandFactory = new LambdaVisitor<CommandSpecification, Command>().
                on(InitHamsterCommandSpecification.class).then(this::createInitHamsterCommand).
                on(PickGrainCommandSpecification.class).then(this::createPickGrainCommand).
                on(PutGrainCommandSpecification.class).then(this::createPutGrainCommand).
                on(MoveCommandSpecification.class).then(this::createMoveCommand);
    }

    private Command createInitHamsterCommand(final InitHamsterCommandSpecification specification) {
        return new CompositeCommand() {
            @Override
            protected void buildBeforeFirstExecution(final CompositeCommandBuilder builder) {
                final Tile tile = getTileAt(specification.getLocation());
                builder.newSetPropertyCommand(specification.getHamster().currentTile, Optional.of(tile));
                builder.newAddToPropertyCommand(tile.content, specification.getHamster());
            }
        };
    }
    
    private Command createPickGrainCommand(final PickGrainCommandSpecification specification) {
        return new CompositeCommand() {
            @Override
            protected void buildBeforeFirstExecution(final CompositeCommandBuilder builder) {
                assert specification.getHamster().getCurrentTile().isPresent();
                final Tile currentTile = specification.getHamster().getCurrentTile().get();
                
                assert currentTile.content.contains(specification.getGrain());

                builder.newRemoveFromPropertyCommand(currentTile.content, specification.getGrain());
                builder.newSetPropertyCommand(specification.getGrain().currentTile, Optional.empty());
            }
        };
    }

    private Command createPutGrainCommand(final PutGrainCommandSpecification specification) {
        return new CompositeCommand() {
            @Override
            protected void buildBeforeFirstExecution(final CompositeCommandBuilder builder) {
                assert specification.getHamster().getCurrentTile().isPresent();
                final Tile currentTile = specification.getHamster().getCurrentTile().get();
                builder.newAddToPropertyCommand(currentTile.content, specification.getGrain());
                builder.newSetPropertyCommand(specification.getGrain().currentTile, Optional.of(currentTile));
            }
        };
    }
    
    private Command createMoveCommand(final MoveCommandSpecification specification) {
        return new CompositeCommand() {
            @Override
            protected void buildBeforeFirstExecution(final CompositeCommandBuilder builder) {
                assert specification.getHamster().getCurrentTile().isPresent();
                assert specification.getHamster().getCurrentTerritory() == GameTerritory.this;

                final LocationVector movementVector = specification.getHamster().getDirection().getMovementVector();
                final Location newHamsterPosition = specification.getHamster().getCurrentTile().get().getLocation().translate(movementVector);

                assert specification.getHamster().getCurrentTerritory().isLocationInTerritory(newHamsterPosition);
                final Tile newTile = specification.getHamster().getCurrentTerritory().getTileAt(newHamsterPosition);

                builder.newRemoveFromPropertyCommand(specification.getHamster().getCurrentTile().get().content, specification.getHamster());
                builder.newSetPropertyCommand(specification.getHamster().currentTile, Optional.of(newTile));
                builder.newAddToPropertyCommand(newTile.content, specification.getHamster());
            }
        };
    }
    
    @Override
    public GameHamster getDefaultHamster() {
        return (GameHamster) this.defaultHamster.get();
    }

    @Override
    public Optional<Command> getCommandFromSpecification(final CommandSpecification spec) {
        final Optional<Command> editorCommand = super.getCommandFromSpecification(spec);
        if (editorCommand.isPresent()) {
            return editorCommand;
        }
        return Optional.ofNullable(this.editCommandFactory.apply(spec));
    }

    @Override
    protected ReadOnlyHamster initDefaultHamster() {
        return new GameHamster();
    }
}
