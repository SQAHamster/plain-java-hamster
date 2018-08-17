package de.unistuttgart.iste.rss.oo.hamstersimulator.commands;

import static de.unistuttgart.iste.rss.oo.hamstersimulator.util.Preconditions.checkArgument;

import java.util.LinkedList;
import java.util.List;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.specification.CommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.specification.CompositeCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.specification.SpecifiedCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.external.model.WriteCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.AddGrainsToTileCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.AddWallToTileCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.ClearTileCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.InitHamsterCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.InitializeTerritoryCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.Territory;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.command.specification.AddGrainsToTileCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.command.specification.AddWallToTileCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.command.specification.ClearTileCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.command.specification.InitHamsterCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.command.specification.InitializeTerritoryCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.command.specification.SpecificationVisitor;

public class EditCommandStack<T extends Command> extends CommandStack<T> {

    private class CloneVisitor implements SpecificationVisitor {

        private Command result;
        private final Territory territory;


        public CloneVisitor(final Territory territory) {
            super();
            this.territory = territory;
        }

        @Override
        public void visit(final AddGrainsToTileCommandSpecification addGrainsToTileCommandSpecification) {
            try {
                result = new AddGrainsToTileCommand(territory, (AddGrainsToTileCommandSpecification) addGrainsToTileCommandSpecification.clone());
            } catch (final CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void visit(final AddWallToTileCommandSpecification addWallToTileCommandSpecification) {
            try {
                result = new AddWallToTileCommand(territory, (AddWallToTileCommandSpecification) addWallToTileCommandSpecification.clone());
            } catch (final CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void visit(final ClearTileCommandSpecification clearTileCommandSpecification) {
            try {
                result = new ClearTileCommand(territory, (ClearTileCommandSpecification) clearTileCommandSpecification.clone());
            } catch (final CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void visit(final InitializeTerritoryCommandSpecification initializeTerritoryCommandSpecification) {
            try {
                result = new InitializeTerritoryCommand(territory, (InitializeTerritoryCommandSpecification) initializeTerritoryCommandSpecification.clone());
            } catch (final CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void visit(final InitHamsterCommandSpecification initHamsterCommandSpecification) {
            try {
                result = new InitHamsterCommand(territory, (InitHamsterCommandSpecification) initHamsterCommandSpecification.clone());
            } catch (final CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void visit(final WriteCommandSpecification writeCommandSpecification) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void visit(final CompositeCommandSpecification compositeCommandSpecification) {
            final List<Command> copiedCommands = new LinkedList<>();

            for (final CommandSpecification childSpec : compositeCommandSpecification.getSpecifications()) {
                final CloneVisitor visitor = new CloneVisitor(territory);
                childSpec.visit(visitor);
                copiedCommands.add(visitor.result);
            }
            result = new AbstractCompositeCommand() {
                @Override
                protected void buildBeforeFirstExecution(final CompositeCommandBuilder builder) {
                    builder.add(copiedCommands);
                }
            };
        }
    }

    @Override
    public void execute(final T command) {
        checkArgument(command instanceof SpecifiedCommand<?>, "Only specified commands may be executed in edit mode!");
        super.execute(command);
    }

    public Command cloneCommandsInto(final Territory territory) {
        final List<Command> copiedCommands = new LinkedList<>();

        for (final Command command : this.executedCommands) {
            final SpecifiedCommand<?> currentCommand = (SpecifiedCommand<?>) command;
            final CloneVisitor visitor = new CloneVisitor(territory);
            currentCommand.getSpecification().visit(visitor);
            copiedCommands.add(visitor.result);
        }

        return new AbstractCompositeCommand() {
            @Override
            protected void buildBeforeFirstExecution(final CompositeCommandBuilder builder) {
                builder.add(copiedCommands);
            }
        };
    }
}
