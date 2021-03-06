package de.hamstersimulator.objectsfirst.internal.model.territory;

import static de.hamstersimulator.objectsfirst.utils.Preconditions.checkNotNull;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import de.hamstersimulator.objectsfirst.commands.Command;
import de.hamstersimulator.objectsfirst.commands.CommandSpecification;
import de.hamstersimulator.objectsfirst.commands.CompositeCommand;
import de.hamstersimulator.objectsfirst.datatypes.Direction;
import de.hamstersimulator.objectsfirst.datatypes.Location;
import de.hamstersimulator.objectsfirst.datatypes.Size;
import de.hamstersimulator.objectsfirst.internal.model.territory.command.specification.AddGrainsToTileCommandSpecification;
import de.hamstersimulator.objectsfirst.internal.model.territory.command.specification.AddWallToTileCommandSpecification;
import de.hamstersimulator.objectsfirst.internal.model.territory.command.specification.InitDefaultHamsterCommandSpecification;
import de.hamstersimulator.objectsfirst.internal.model.territory.command.specification.InitializeTerritoryCommandSpecification;

/**
 * Objects of this class can be used to define Hamster territories. The builder
 * objects track all operations used to define the territory and finally create
 * a large composite command that initializes the defined territory.
 *
 * @author Steffen Becker
 *
 */
public class TerritoryBuilder {

    private final EditorTerritory territory;
    private final LinkedList<Command> commands = new LinkedList<>();

    private final List<Consumer<CompositeCommand.PreconditionBuilder>> preconditionCreators = new LinkedList<>();

    TerritoryBuilder(final EditorTerritory territory) {
        super();
        this.territory = territory;
    }

    /**
     * Factory method to create new TerritoryBuilder objects which then can be
     * used to defined the given territory.
     * @param territory The territory which should be built.
     * @return A TerritoryBuilder object for the given territory.
     */
    public static TerritoryBuilder getTerritoryBuilderForTerritory(final EditorTerritory territory) {
        return new TerritoryBuilder(territory);
    }

    /**
     * Defines a new territory by deleting all elements of the territory defined so far
     * and by creating a new, empty territory of the given size.
     * @param size The size to which the territory should be initialized
     * @return The builder again for a fluent API use case
     */
    public TerritoryBuilder initializeTerritory(final Size size) {
        this.commands.add(this.territory.getCommandFromSpecification(new InitializeTerritoryCommandSpecification(size)).get());
        return this;
    }

    /**
     * Place a wall at the given location. The location has to be empty before
     * a wall can be placed.
     * @param location The location of the new wall.
     * @return The builder again for a fluent API use case.
     */
    public TerritoryBuilder wallAt(final Location location) {
        this.commands.add(this.territory.getCommandFromSpecification(new AddWallToTileCommandSpecification(location)).get());
        return this;
    }

    /**
     * Puts the default hamster (paule) on the territory and initializes it. The location
     * of the hamster must not be blocked by a wall.
     * @param location Location where the default hamster will be placed.
     * @param direction The direction into which the default hamster will look.
     * @param grainCount The initial amount of grains the default hamster carries in his mouth.
     * @return The builder again for a fluent API use case.
     */
    public TerritoryBuilder defaultHamsterAt(final Location location, final Direction direction, final int grainCount) {
        final CommandSpecification specification = new InitDefaultHamsterCommandSpecification(location, direction, grainCount);
        this.commands.add(this.territory.getCommandFromSpecification(specification).get());
        this.commands.add(this.territory.getDefaultHamster().getCommandFromSpecification(specification).get());
        return this;
    }

    /**
     * Puts a number of grains at the given location. The location
     * must not be blocked by a wall.
     * @param location Location where the new grains will be put at.
     * @param grainCount The number of new grains to put.
     * @return The builder again for a fluent API use case.
     */
    public TerritoryBuilder grainAt(final Location location, final int grainCount) {
        this.commands.add(this.territory.getCommandFromSpecification(new AddGrainsToTileCommandSpecification(location, grainCount)).get());
        return this;
    }

    /**
     * Puts a single grain at the given location. The location
     * must not be blocked by a wall.
     * @param location Location where the new grain will be put at.
     * @return The builder again for a fluent API use case.
     */
    public TerritoryBuilder grainAt(final Location location) {
        return this.grainAt(location, 1);
    }

    /**
     * Executing this command returns a composite command which initializes the
     * territory according to all builder commands issued before. It has to be the
     * last command used on this builder object. After calling build, this TerritoryBuilder
     * should not be used any longer.
     * Also applies all preconditions to this command
     * @return A command which, when executed on an editable territory, initializes that territory.
     */
    public Command build() {
        return new CompositeCommand() {
            @Override
            protected void buildBeforeFirstExecution(final CompositeCommandBuilder builder) {
                builder.add(commands);
            }
        }.setPreconditionConstructor(builder -> {
            for (final Consumer<CompositeCommand.PreconditionBuilder> preconditionCreators : preconditionCreators) {
                preconditionCreators.accept(builder);
            }
        });
    }

    /*@
     @ requires preconditionCreator != null;
     @*/
    /**
     * Adds a Consumer which can add preconditions to a CompositeCommand via a PreconditionBuilder
     * @param preconditionCreator the consumer which has to handle the PreconditionBuilder
     */
    public void addPreconditionCreator(final Consumer<CompositeCommand.PreconditionBuilder> preconditionCreator) {
        checkNotNull(preconditionCreator);

        this.preconditionCreators.add(preconditionCreator);
    }

}
