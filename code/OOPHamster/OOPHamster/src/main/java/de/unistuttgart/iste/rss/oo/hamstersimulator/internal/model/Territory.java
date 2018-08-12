package de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model;

import static de.unistuttgart.iste.rss.oo.hamstersimulator.util.Preconditions.checkArgument;

import java.awt.Dimension;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.Command;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.CommandStack;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;

public class Territory {

    final ReadOnlyObjectWrapper<Dimension> territorySize = new ReadOnlyObjectWrapper<Dimension>(this, "territorySize", new Dimension(0, 0));
    final ReadOnlyListWrapper<Tile> tiles = new ReadOnlyListWrapper<Tile>(this, "tiles", FXCollections.observableArrayList());

    private final Hamster defaultHamster;
    private final CommandStack<? extends Command> commandStack;

    /**
     *
     * @param editStack
     * @param territoryFileName
     */
    public Territory(final CommandStack<Command> commandStack) {
        super();

        this.defaultHamster = new Hamster();
        this.commandStack = commandStack;
    }

    @SuppressWarnings("unchecked")
    public <T extends Command> CommandStack<T> getCommandStack() {
        return (CommandStack<T>) commandStack;
    }

    public int getRowCount() {
        return this.territorySize.get().height;
    }

    public int getColumnCount() {
        return this.territorySize.get().width;
    }

    public Tile getTileAt(final Location location) {
        checkArgument(isLocationInTerritory(location), "Location has to be in territory!");

        return tiles.get(getListIndexFromLocation(location));
    }

    public Hamster getDefaultHamster() {
        return this.defaultHamster;
    }

    public boolean isLocationInTerritory(final Location newHamsterPosition) {
        return newHamsterPosition.getColumn() < this.getColumnCount() &&
                newHamsterPosition.getRow() < this.getRowCount();
    }

    public ReadOnlyObjectProperty<Dimension> territorySizeProperty() {
        return this.territorySize.getReadOnlyProperty();
    }

    public ReadOnlyListProperty<Tile> tilesProperty() {
        return this.tiles.getReadOnlyProperty();
    }

    public TerritoryBuilder getTerritoryBuilder() {
        return new TerritoryBuilder(this);
    }

    Stream<Location> getAllLocationsFromTo(final Location from, final Location to) {
        final Stream<Stream<Location>> stream = IntStream.range(from.getRow(), to.getRow()+1).mapToObj(row -> IntStream.range(from.getColumn(), to.getColumn()+1).mapToObj(column -> Location.from(row, column)));
        return stream.flatMap(s -> s);
    }

    private int getListIndexFromLocation(final Location location) {
        return location.getRow() * this.getColumnCount() + location.getColumn();
    }

    public Dimension getSize() {
        return this.territorySize.get();
    }
}