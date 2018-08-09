package de.unistuttgart.iste.rss.oo.hamstersimulator.territory.commands;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.function.Consumer;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.PropertyMap;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.UnidirectionalUpdatePropertyCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.specification.PropertyCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.specification.PropertyCommandSpecification.ActionKind;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.Territory;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.tile.Tile;

public class SetTerritorySizeCommand extends UnidirectionalUpdatePropertyCommand<Territory> {

    public SetTerritorySizeCommand(final PropertyMap<Territory> entityState, final Dimension newDimension) {
        super(entityState, new PropertyCommandSpecification("territorySize", newDimension, ActionKind.SET));
    }

    @Override
    public void execute() {
        disposeAllExistingTiles();
        super.execute();
        initNewTileStore((Dimension) getSpecification().getNewValue());
        createNewTiles();
    }

    private void initNewTileStore(final Dimension newDimension) {
        this.entityState.<Tile> getListProperty("tiles").clear();
    }

    private void disposeAllExistingTiles() {
        forAllTilesDo(t -> {
            t.dispose();
            this.entityState.<Tile> getListProperty("tiles").remove(t);
        });
    }

    private void forAllTilesDo(final Consumer<Tile> operation) {
        new ArrayList<>(this.entityState.<Tile> getListProperty("tiles").get()).forEach(tile -> operation.accept(tile));
    }

    private void createNewTiles() {
        for (int row = 0; row < this.entityState.<Dimension> getObjectProperty("territorySize").get().height; row++) {
            for (int column = 0; column < this.entityState.<Dimension> getObjectProperty("territorySize").get().width; column++) {
                final Tile newTile = Tile.createEmptyTile(this.entityState.getPropertyOwner(), Location.from(row, column));
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
