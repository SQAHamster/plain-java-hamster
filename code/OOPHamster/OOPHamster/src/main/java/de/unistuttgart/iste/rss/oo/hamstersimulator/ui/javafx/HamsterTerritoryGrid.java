package de.unistuttgart.iste.rss.oo.hamstersimulator.ui.javafx;

import java.util.HashMap;
import java.util.Map;

import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Size;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.Hamster;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.Territory;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.Tile;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.geometry.Pos;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

public class HamsterTerritoryGrid extends GridPane {

    final Map<Hamster,Integer> hamsterToColorPos = new HashMap<>();

    private static final double TILE_SIZE = 40.0;
    private static final double INSET = TILE_SIZE / 2.0;

    private final SimpleObjectProperty<Size> gridSize = new SimpleObjectProperty<Size>(this, "gridSize", new Size(0,0));
    private final ReadOnlyListWrapper<TileNode> cells = new ReadOnlyListWrapper<TileNode>(this, "cells", FXCollections.observableArrayList());
    private Territory territory;

    private final ListChangeListener<Tile> tilesChangedListener = new ListChangeListener<Tile>() {
        @Override
        public void onChanged(final Change<? extends Tile> change) {
            while (change.next()) {
                if (change.wasAdded()) {
                    change.getAddedSubList().forEach(tile -> addTileNode(tile));
                }
                if (change.wasRemoved()) {
                    change.getRemoved().forEach(tile -> removeTileNode(tile));
                }
            }
        }
    };

    public HamsterTerritoryGrid() {
        super();
        this.getStyleClass().add("game-grid");
        configureSquareSizedTiles(this.gridSize.get());
        this.gridSize.addListener((obj, oldValue, newValue) -> {
            configureSquareSizedTiles(newValue);
        });
    }

    public void bindToTerritory(final Territory territory) {
        this.territory = territory;
        this.gridSize.bind(this.territory.territorySizeProperty());
        this.territory.tilesProperty().addListener(tilesChangedListener);
    }

    public void unbind() {
        this.gridSize.unbind();
        this.territory.tilesProperty().removeListener(tilesChangedListener);
    }

    private void configureSquareSizedTiles(final Size size) {
        final int columns = size.getColumnCount();
        final int rows = size.getRowCount();
        for (int i = 0; i < columns; i++) {
            final ColumnConstraints column = new ColumnConstraints();
            column.setPercentWidth(100.0 / columns);
            this.getColumnConstraints().add(column);
        }

        for (int i = 0; i < rows; i++) {
            final RowConstraints row = new RowConstraints(TILE_SIZE);
            row.setPercentHeight(100.0 / rows);
            this.getRowConstraints().add(row);
        }
        this.setAlignment(Pos.CENTER);
        computeNewMinimumSize(size);
    }

    private void computeNewMinimumSize(final Size size) {
        final int columns = size.getColumnCount();
        final int rows = size.getRowCount();
        this.setMinSize(TILE_SIZE * columns + INSET, TILE_SIZE * rows + INSET);
        this.setMaxSize(TILE_SIZE * columns + INSET, TILE_SIZE * rows + INSET);
    }

    private void addTileNode(final Tile tile) {
        final Location location = tile.getLocation();
        setTileNodeAt(location, new TileNode(this, tile));
    }

    private void removeTileNode(final Tile tile) {
        getTileNodeAt(tile.getLocation()).dispose();
        setTileNodeAt(tile.getLocation(), null);
        Platform.runLater(() -> this.getChildren().remove(getTileNodeAt(tile.getLocation())));
    }

    private TileNode getTileNodeAt(final Location location) {
        return this.cells.get(location.getRow() * this.gridSize.get().getColumnCount() + location.getColumn());
    }

    private void setTileNodeAt(final Location location, final TileNode node) {
        final int index = location.getRow() * this.gridSize.get().getColumnCount() + location.getColumn();
        if (index < this.cells.getSize()) {
            this.cells.set(index, node);
        } else {
            this.cells.add(index, node);
        }
        if (node != null) {
            Platform.runLater(() -> this.add(node, location.getColumn(), location.getRow()));
        }
    }

}
