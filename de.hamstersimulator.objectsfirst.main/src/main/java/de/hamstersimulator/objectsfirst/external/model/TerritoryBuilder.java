package de.hamstersimulator.objectsfirst.external.model;

import de.hamstersimulator.objectsfirst.commands.Command;
import de.hamstersimulator.objectsfirst.datatypes.Direction;
import de.hamstersimulator.objectsfirst.datatypes.Location;
import de.hamstersimulator.objectsfirst.datatypes.Mode;
import de.hamstersimulator.objectsfirst.datatypes.Size;

public class TerritoryBuilder {
    private final de.hamstersimulator.objectsfirst.internal.model.territory.TerritoryBuilder decoratedBuilder;

    private TerritoryBuilder(
            final de.hamstersimulator.objectsfirst.internal.model.territory.TerritoryBuilder internalBuilder) {
        this.decoratedBuilder = internalBuilder;
    }

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
     * @param size
     * @return
     * @see de.hamstersimulator.objectsfirst.internal.model.territory.TerritoryBuilder#initializeTerritory(de.hamstersimulator.objectsfirst.datatypes.Size)
     */
    public TerritoryBuilder initializeTerritory(Size size) {
        decoratedBuilder.initializeTerritory(size);
        return this;
    }

    /**
     * @param location
     * @return
     * @see de.hamstersimulator.objectsfirst.internal.model.territory.TerritoryBuilder#wallAt(de.hamstersimulator.objectsfirst.datatypes.Location)
     */
    public TerritoryBuilder wallAt(Location location) {
        decoratedBuilder.wallAt(location);
        return this;
    }

    /**
     * @param location
     * @param direction
     * @param grainCount
     * @return
     * @see de.hamstersimulator.objectsfirst.internal.model.territory.TerritoryBuilder#defaultHamsterAt(de.hamstersimulator.objectsfirst.datatypes.Location, de.hamstersimulator.objectsfirst.datatypes.Direction, int)
     */
    public TerritoryBuilder defaultHamsterAt(Location location, Direction direction, int grainCount) {
        decoratedBuilder.defaultHamsterAt(location, direction, grainCount);
        return this;
    }

    /**
     * @param location
     * @param grainCount
     * @return
     * @see de.hamstersimulator.objectsfirst.internal.model.territory.TerritoryBuilder#grainAt(de.hamstersimulator.objectsfirst.datatypes.Location, int)
     */
    public TerritoryBuilder grainAt(Location location, int grainCount) {
        decoratedBuilder.grainAt(location, grainCount);
        return this;
    }

    /**
     * @param location
     * @return
     * @see de.hamstersimulator.objectsfirst.internal.model.territory.TerritoryBuilder#grainAt(de.hamstersimulator.objectsfirst.datatypes.Location)
     */
    public TerritoryBuilder grainAt(Location location) {
        decoratedBuilder.grainAt(location);
        return this;
    }

    /**
     * @return
     * @see de.hamstersimulator.objectsfirst.internal.model.territory.TerritoryBuilder#build()
     */
    public Command build() {
        return decoratedBuilder.build();
    }

}
