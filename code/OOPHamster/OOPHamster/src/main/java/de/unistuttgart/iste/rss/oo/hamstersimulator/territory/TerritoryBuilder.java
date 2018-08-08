package de.unistuttgart.iste.rss.oo.hamstersimulator.territory;

import java.util.Optional;

import de.unistuttgart.iste.rss.oo.hamstersimulator.HamsterSimulator;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Direction;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.tile.Wall;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.tile.commands.AddGrainsToTileCommand;

public class TerritoryBuilder {

    private final Territory territory;
    private final HamsterSimulator simulator;

    public TerritoryBuilder(final HamsterSimulator simulator, final Territory territory) {
        super();
        this.territory = territory;
        this.simulator = simulator;
    }

    public TerritoryBuilder wallAt(final Location location) {
        this.simulator.getCommandStack().execute(this.territory.getTileAt(location).getAddContentCommand(new Wall()));
        return this;
    }

    public TerritoryBuilder defaultHamsterAt(final int row, final int column, final Direction direction, final int grainCount) {
        this.territory.getDefaultHamster().init(
                Optional.of(this.territory.getTileAt(new Location(row, column))),
                direction,
                grainCount);
        return this;
    }

    public TerritoryBuilder grainAt(final Location location) {
        return this.grainAt(location, 1);
    }

    public TerritoryBuilder grainAt(final Location location, final int grainCount) {
        this.simulator.getCommandStack().execute(new AddGrainsToTileCommand(
                this.territory.getTileAt(location), grainCount));
        return this;
    }

}
