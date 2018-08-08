package de.unistuttgart.iste.rss.oo.hamstersimulator.territory;

import static com.google.common.base.Preconditions.checkArgument;

import java.awt.Dimension;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.Command;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.PropertyMap;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;
import de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.Hamster;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.commands.AddContentToTileCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.commands.ModifyContentOfTileCommandParameter;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.commands.RemoveContentFromTileCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.commands.SetTerritorySizeCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.tile.Tile;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.tile.TileContent;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;

public class Territory {

    private final ReadOnlyObjectWrapper<Dimension> territorySize = new ReadOnlyObjectWrapper<Dimension>(this, "territorySize", new Dimension(0, 0));
    private final ReadOnlyListWrapper<Tile> tiles = new ReadOnlyListWrapper<Tile>(this, "tiles", FXCollections.observableArrayList());
    private final PropertyMap<Territory> territoryState = new PropertyMap<Territory>(this, territorySize, tiles);

    private final Hamster defaultHamster;

    /**
     *
     * @param territoryFileName
     */
    public Territory() {
        super();

        this.defaultHamster = new Hamster();
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

    public Command getSetTerritorySizeCommand(final Dimension newDimension) {
        checkArgument(newDimension.width >= 0 && newDimension.height >= 0, "New Territory dimensions need to be positive!");
        return new SetTerritorySizeCommand(this.territoryState, newDimension);
    }

    public Command getAddContentCommand(final Location location, final TileContent content) {
        return new AddContentToTileCommand(this, new ModifyContentOfTileCommandParameter(location, content));
    }

    public Command getRemoveContentCommand(final Location location, final TileContent content) {
        return new RemoveContentFromTileCommand(this, new ModifyContentOfTileCommandParameter(location, content));
    }

    public ReadOnlyObjectProperty<Dimension> territorySizeProperty() {
        return this.territorySize.getReadOnlyProperty();
    }

    public ReadOnlyListProperty<Tile> tilesProperty() {
        return this.tiles.getReadOnlyProperty();
    }

    TerritoryBuilder getTerritoryBuilder() {
        return new TerritoryBuilder(this);
    }

    private int getListIndexFromLocation(final Location location) {
        return location.getRow() * this.getColumnCount() + location.getColumn();
    }
}