package de.unistuttgart.iste.rss.oo.hamstersimulator.territory;

import de.unistuttgart.iste.rss.oo.hamstersimulator.HamsterSimulator;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Direction;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;

public class TerritoryBuilder {

    private final Territory territory;
    private final HamsterSimulator simulator;

    public TerritoryBuilder(final HamsterSimulator simulator, final Territory territory) {
        super();
        this.simulator = simulator;
        this.territory = territory;
    }

    public TerritoryBuilder wallAt(final int row, final int column) {
        this.territory.getTileAt(new Location(row, column)).addObjectToContent(new Wall());
        return this;
    }

    public TerritoryBuilder defaultHamsterAt(final int row, final int column, final Direction direction, final int grainCount) {
        this.territory.getDefaultHamster().getCurrentPosition().ifPresent(pos -> territory.getTileAt(pos).removeObjectFromContent(this.territory.getDefaultHamster()));
        this.territory.getDefaultHamster().init(
                Location.from(row, column),
                direction,
                grainCount);
        this.territory.getTileAt(new Location(row, column)).addObjectToContent(this.territory.getDefaultHamster());
        return this;
    }

    public TerritoryBuilder grainAt(final int row, final int column) {
        return this.grainAt(row,column,1);
    }

    public TerritoryBuilder grainAt(final int row, final int column, final int grainCount) {
        this.putNewGrain(this.territory.getTileAt(new Location(row, column)), grainCount);
        return this;
    }

    private void putNewGrain(final Tile tile, final int count) {
        for (int i = 0; i < count; i++) {
            tile.addObjectToContent(new Grain());
        }
    }
}
