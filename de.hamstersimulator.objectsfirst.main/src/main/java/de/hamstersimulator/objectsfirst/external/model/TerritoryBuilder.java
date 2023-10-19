package de.hamstersimulator.objectsfirst.external.model;

import de.hamstersimulator.objectsfirst.commands.Command;
import de.hamstersimulator.objectsfirst.datatypes.Direction;
import de.hamstersimulator.objectsfirst.datatypes.Location;
import de.hamstersimulator.objectsfirst.datatypes.Mode;
import de.hamstersimulator.objectsfirst.datatypes.Size;

/**
 * Objects of this class can be used to define Hamster territories. The builder
 * objects track all operations used to define the territory and finally create
 * a large composite command that initializes the defined territory.
 * Wrapper around the internal builder.
 *
 * @author Steffen Becker
 * @see de.hamstersimulator.objectsfirst.internal.model.territory.TerritoryBuilder
 */
public class TerritoryBuilder {
    private final de.hamstersimulator.objectsfirst.internal.model.territory.TerritoryBuilder decoratedBuilder;

    private TerritoryBuilder(
            final de.hamstersimulator.objectsfirst.internal.model.territory.TerritoryBuilder internalBuilder) {
        this.decoratedBuilder = internalBuilder;
    }

    /**
     * Factory method to create new TerritoryBuilder objects which then can be
     * used to defined the given territory.
     * @param territory The territory which should be built.
     * @return A TerritoryBuilder object for the given territory.
     */
    static TerritoryBuilder getTerritoryBuilderForTerritory(final Territory territory) {
        var internalBuilder = de.hamstersimulator.objectsfirst.internal.model.territory.TerritoryBuilder
                .getTerritoryBuilderForTerritory(territory.getInternalTerritory());
        internalBuilder.addPreconditionCreator(builder -> {
            builder.addNewPrecondition(IllegalStateException::new,
                    () -> territory.getGame().getCurrentGameMode() != Mode.INITIALIZING);
        });
        return new TerritoryBuilder(internalBuilder);
    }

    /**
     * Defines a new territory by deleting all elements of the territory defined so far
     * and by creating a new, empty territory of the given size.
     * @param size The size to which the territory should be initialized
     * @return The builder again for a fluent API use case
     * @see de.hamstersimulator.objectsfirst.internal.model.territory.TerritoryBuilder#initializeTerritory(de.hamstersimulator.objectsfirst.datatypes.Size)
     */
    public TerritoryBuilder initializeTerritory(Size size) {
        decoratedBuilder.initializeTerritory(size);
        return this;
    }

    /**
     * Place a wall at the given location. The location has to be empty before
     * a wall can be placed.
     * @param location The location of the new wall.
     * @return The builder again for a fluent API use case.
     * @see de.hamstersimulator.objectsfirst.internal.model.territory.TerritoryBuilder#wallAt(de.hamstersimulator.objectsfirst.datatypes.Location)
     */
    public TerritoryBuilder wallAt(Location location) {
        decoratedBuilder.wallAt(location);
        return this;
    }

    /**
     * Puts the default hamster (paule) on the territory and initializes it. The location
     * of the hamster must not be blocked by a wall.
     * @param location Location where the default hamster will be placed.
     * @param direction The direction into which the default hamster will look.
     * @param grainCount The initial amount of grains the default hamster carries in his mouth.
     * @return The builder again for a fluent API use case.
     * @see de.hamstersimulator.objectsfirst.internal.model.territory.TerritoryBuilder#defaultHamsterAt(de.hamstersimulator.objectsfirst.datatypes.Location, de.hamstersimulator.objectsfirst.datatypes.Direction, int)
     */
    public TerritoryBuilder defaultHamsterAt(Location location, Direction direction, int grainCount) {
        decoratedBuilder.defaultHamsterAt(location, direction, grainCount);
        return this;
    }

    /**
     * Puts a number of grains at the given location. The location
     * must not be blocked by a wall.
     * @param location Location where the new grains will be put at.
     * @param grainCount The number of new grains to put.
     * @return The builder again for a fluent API use case.
     * @see de.hamstersimulator.objectsfirst.internal.model.territory.TerritoryBuilder#grainAt(de.hamstersimulator.objectsfirst.datatypes.Location, int)
     */
    public TerritoryBuilder grainAt(Location location, int grainCount) {
        decoratedBuilder.grainAt(location, grainCount);
        return this;
    }

    /**
     * Puts a single grain at the given location. The location
     * must not be blocked by a wall.
     * @param location Location where the new grain will be put at.
     * @return The builder again for a fluent API use case.
     * @see de.hamstersimulator.objectsfirst.internal.model.territory.TerritoryBuilder#grainAt(de.hamstersimulator.objectsfirst.datatypes.Location)
     */
    public TerritoryBuilder grainAt(Location location) {
        decoratedBuilder.grainAt(location);
        return this;
    }

    /**
     * Executing this command returns a composite command which initializes the
     * territory according to all builder commands issued before. It has to be the
     * last command used on this builder object. After calling build, this TerritoryBuilder
     * should not be used any longer.
     * Also applies all preconditions to this command
     * @return A command which, when executed on an editable territory, initializes that territory.
     * @see de.hamstersimulator.objectsfirst.internal.model.territory.TerritoryBuilder#build()
     */
    public Command build() {
        return decoratedBuilder.build();
    }

}
