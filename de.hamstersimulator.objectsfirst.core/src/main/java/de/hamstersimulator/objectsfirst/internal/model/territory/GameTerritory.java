package de.hamstersimulator.objectsfirst.internal.model.territory;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import de.hamstersimulator.objectsfirst.commands.Command;
import de.hamstersimulator.objectsfirst.commands.CommandSpecification;
import de.hamstersimulator.objectsfirst.commands.CompositeCommand;
import de.hamstersimulator.objectsfirst.datatypes.Direction;
import de.hamstersimulator.objectsfirst.datatypes.Location;
import de.hamstersimulator.objectsfirst.datatypes.LocationVector;
import de.hamstersimulator.objectsfirst.exceptions.FrontBlockedException;
import de.hamstersimulator.objectsfirst.exceptions.HamsterNotInitializedException;
import de.hamstersimulator.objectsfirst.exceptions.MouthEmptyException;
import de.hamstersimulator.objectsfirst.exceptions.NoGrainOnTileException;
import de.hamstersimulator.objectsfirst.internal.model.hamster.GameHamster;
import de.hamstersimulator.objectsfirst.internal.model.hamster.ReadOnlyHamster;
import de.hamstersimulator.objectsfirst.internal.model.hamster.command.specification.InitHamsterCommandSpecification;
import de.hamstersimulator.objectsfirst.internal.model.hamster.command.specification.MoveCommandSpecification;
import de.hamstersimulator.objectsfirst.internal.model.hamster.command.specification.PickGrainCommandSpecification;
import de.hamstersimulator.objectsfirst.internal.model.hamster.command.specification.PutGrainCommandSpecification;
import de.hamstersimulator.objectsfirst.utils.LambdaVisitor;

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
                builder.newAddToPropertyCommand(hamsters, specification.getHamster());
            }
        };
    }

    private Command createPickGrainCommand(final PickGrainCommandSpecification specification) {
        return new CompositeCommand().setCommandConstructor(builder -> {
                assert specification.getHamster().getCurrentTile().isPresent();
                final Tile currentTile = specification.getHamster().getCurrentTile().get();

                assert currentTile.content.contains(specification.getGrain());

                builder.newRemoveFromPropertyCommand(currentTile.content, specification.getGrain());
                builder.newSetPropertyCommand(specification.getGrain().currentTile, Optional.empty());
            }
        ).setPreconditionConstructor(builder -> {
            builder.addNewPrecondition(HamsterNotInitializedException::new, () -> !specification.getHamster().getCurrentTile().isPresent());
            builder.addNewPrecondition(NoGrainOnTileException::new, () -> !specification.getHamster().getCurrentTile().get().content.contains(specification.getGrain()));
        });
    }

    private Command createPutGrainCommand(final PutGrainCommandSpecification specification) {
        return new CompositeCommand().setCommandConstructor(builder -> {
                assert specification.getHamster().getCurrentTile().isPresent();
                final Tile currentTile = specification.getHamster().getCurrentTile().get();
                builder.newSetPropertyCommand(specification.getGrain().currentTile, Optional.of(currentTile));
                builder.newAddToPropertyCommand(currentTile.content, specification.getGrain());
            }
        ).setPreconditionConstructor(builder -> {
            builder.addNewPrecondition(MouthEmptyException::new, specification.getHamster().getGrainInMouth()::isEmpty);
        });
    }

    private Command createMoveCommand(final MoveCommandSpecification specification) {

        final GameHamster hamster = specification.getHamster();
        final ReadOnlyTerritory territory = hamster.getCurrentTerritory();
        final Supplier<Location> newLocation = () -> {
            final LocationVector movementVector = hamster.getDirection().getMovementVector();
            return hamster.getCurrentTile().get().getLocation().translate(movementVector);
        };

        return new CompositeCommand().setCommandConstructor(builder -> {
                assert specification.getHamster().getCurrentTerritory() == GameTerritory.this;

                final Location newHamsterPosition = newLocation.get();
                final Tile newTile = territory.getTileAt(newHamsterPosition);

                builder.newRemoveFromPropertyCommand(specification.getHamster().getCurrentTile().get().content, specification.getHamster());
                builder.newSetPropertyCommand(specification.getHamster().currentTile, Optional.of(newTile));
                builder.newAddToPropertyCommand(newTile.content, specification.getHamster());
            }
        ).setPreconditionConstructor(builder -> {
            builder.addNewPrecondition(HamsterNotInitializedException::new, () -> !specification.getHamster().getCurrentTile().isPresent());
            builder.addNewPrecondition(FrontBlockedException::new,
                    () -> specification.getHamster().getCurrentTile().get().getLocation().getRow() == 0
                                && specification.getHamster().getDirection() == Direction.NORTH);
            builder.addNewPrecondition(FrontBlockedException::new,
                    () -> specification.getHamster().getCurrentTile().get().getLocation().getColumn() == 0
                    && specification.getHamster().getDirection() == Direction.WEST);
            builder.addNewPrecondition(FrontBlockedException::new, () -> !territory.isLocationInTerritory(newLocation.get()));
            builder.addNewPrecondition(FrontBlockedException::new, () -> territory.getTileAt(newLocation.get()).isBlocked());
        });
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
