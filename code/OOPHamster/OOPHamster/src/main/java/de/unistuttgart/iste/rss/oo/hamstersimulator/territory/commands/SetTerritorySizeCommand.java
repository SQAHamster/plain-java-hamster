package de.unistuttgart.iste.rss.oo.hamstersimulator.territory.commands;

import java.awt.Dimension;
import java.util.function.Consumer;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.PropertyMap;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.Territory;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.tile.Tile;

public class SetTerritorySizeCommand extends TerritoryCommand {

    private final Dimension newDimension;
    private Dimension oldDimension;

    public SetTerritorySizeCommand(final PropertyMap<Territory> territoryState, final Dimension newDimension) {
        super(territoryState);
        this.newDimension = newDimension;

    }

    @Override
    protected void execute() {
        this.oldDimension = this.entityState.<Dimension> getObjectProperty("territorySize").get();
        disposeAllExistingTiles();
        initNewTileStore(newDimension);
        createNewTiles();
    }

    @Override
    protected void undo() {
        this.entityState.getObjectProperty("territorySize").set(oldDimension);
    }

    private void initNewTileStore(final Dimension newDimension) {
        this.entityState.getObjectProperty("territorySize").set(newDimension);
        this.entityState.<Tile> getListProperty("tiles").clear();
    }

    private void disposeAllExistingTiles() {
        forAllTilesDo(t -> {
            t.dispose();
            this.entityState.<Tile> getListProperty("tiles").remove(t);
        });
    }

    private void forAllTilesDo(final Consumer<Tile> operation) {
        this.entityState.<Tile> getListProperty("tiles").get().forEach(tile -> operation.accept(tile));
    }

    private void createNewTiles() {
        for (int row = 0; row < this.entityState.<Dimension> getObjectProperty("territorySize").get().height; row++) {
            for (int column = 0; column < this.entityState.<Dimension> getObjectProperty("territorySize").get().width; column++) {
                final Tile newTile = Tile.createEmptyTile(this.getTerritory(), Location.from(row, column));
                setTile(newTile);
            }
        }
    }

    private void setTile(final Tile newTile) {
        this.entityState.<Tile> getListProperty("tiles").add(getListIndexFromLocation(newTile.getLocation()), newTile);
    }

    private int getListIndexFromLocation(final Location location) {
        return location.getRow() * this.entityState.<Dimension> getObjectProperty("territorySize").get().width + location.getColumn();
    }

}
