package de.unistuttgart.iste.rss.oo.hamstersimulator.external.model;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.Command;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Direction;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Size;

public class TerritoryBuilder {
    private final de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.territory.TerritoryBuilder decoratedBuilder;

    private TerritoryBuilder(
            final de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.territory.TerritoryBuilder internalBuilder) {
        this.decoratedBuilder = internalBuilder;
    }

    static TerritoryBuilder getTerritoryBuilderForTerritory(final Territory territory) {
        var internalBuilder = de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.territory.TerritoryBuilder
                .getTerritoryBuilderForTerritory(territory.getInternalTerritory());
        return new TerritoryBuilder(internalBuilder);
    }

    /**
     * @param size
     * @return
     * @see de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.territory.TerritoryBuilder#initializeTerritory(de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Size)
     */
    public TerritoryBuilder initializeTerritory(Size size) {
        decoratedBuilder.initializeTerritory(size);
        return this;
    }

    /**
     * @param location
     * @return
     * @see de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.territory.TerritoryBuilder#wallAt(de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location)
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
     * @see de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.territory.TerritoryBuilder#defaultHamsterAt(de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location, de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Direction, int)
     */
    public TerritoryBuilder defaultHamsterAt(Location location, Direction direction, int grainCount) {
        decoratedBuilder.defaultHamsterAt(location, direction, grainCount);
        return this;
    }

    /**
     * @param location
     * @param grainCount
     * @return
     * @see de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.territory.TerritoryBuilder#grainAt(de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location, int)
     */
    public TerritoryBuilder grainAt(Location location, int grainCount) {
        decoratedBuilder.grainAt(location, grainCount);
        return this;
    }

    /**
     * @param location
     * @return
     * @see de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.territory.TerritoryBuilder#grainAt(de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location)
     */
    public TerritoryBuilder grainAt(Location location) {
        decoratedBuilder.grainAt(location);
        return this;
    }

    /**
     * @return
     * @see de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.territory.TerritoryBuilder#build()
     */
    public Command build() {
        return decoratedBuilder.build();
    }

}
