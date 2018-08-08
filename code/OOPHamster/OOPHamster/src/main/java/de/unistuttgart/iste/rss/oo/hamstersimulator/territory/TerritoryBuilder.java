package de.unistuttgart.iste.rss.oo.hamstersimulator.territory;

import java.util.Optional;

import de.unistuttgart.iste.rss.oo.hamstersimulator.HamsterSimulator;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.Command;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.PropertyCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.PropertyCommandSpecification.ActionKind;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.UnidirectionalUpdatePropertyCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Direction;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.tile.Tile;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.tile.Wall;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.tile.commands.AddGrainsCommand;

public class TerritoryBuilder {

    private final Territory territory;
    private final HamsterSimulator simulator;

    public TerritoryBuilder(final HamsterSimulator simulator, final Territory territory) {
        super();
        this.territory = territory;
        this.simulator = simulator;
    }

    public TerritoryBuilder wallAt(final Location location) {
        final PropertyCommandSpecification spec = new PropertyCommandSpecification("content", new Wall(), ActionKind.ADD);
        final Command command = new UnidirectionalUpdatePropertyCommand<Tile>(this.territory.getTileAt(location).getState(), spec);
        this.simulator.getCommandStack().execute(command);
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
        this.simulator.getCommandStack().execute(new AddGrainsCommand(
                this.territory.getTileAt(location).getState(), grainCount));
        return this;
    }

}
