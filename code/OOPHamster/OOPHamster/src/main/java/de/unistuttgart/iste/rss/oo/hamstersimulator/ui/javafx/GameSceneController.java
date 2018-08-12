package de.unistuttgart.iste.rss.oo.hamstersimulator.ui.javafx;

import java.util.HashMap;
import java.util.Map;

import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.Hamster;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.Territory;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.Tile;
import javafx.beans.binding.DoubleBinding;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

public class GameSceneController {

    final Map<Hamster,Integer> hamsterToColorPos = new HashMap<>();

    private static final double TILE_SIZE = 40.0;
    private static final double INSET = TILE_SIZE / 2.0;

    private TileNode[][] territoryTile;
    private Territory territory;

    @FXML
    private BorderPane root;

    @FXML
    private ToolBar toolbar;

    @FXML
    private Button play;

    @FXML
    private Button pause;

    @FXML
    private Slider speed;

    @FXML
    private GridPane hamsterGrid;

    @FXML
    private void initialize() {
        final DoubleBinding prop = hamsterGrid.minHeightProperty().add(toolbar.heightProperty()).add(30);
        this.root.minHeightProperty().bind(prop);
        this.root.minWidthProperty().bind(hamsterGrid.minWidthProperty());
        this.hamsterGrid.getStyleClass().add("game-grid");
    }

    @FXML
    void pauseGame(final ActionEvent event) {
    }

    @FXML
    void startGame(final ActionEvent event) {
    }

    public void connectToTerritory(final Territory territory) {
        this.territory = territory;
        configureSquareSizedTiles();
        connectToTerritory();
    }

    private void computeNewMinimumSize() {
        final int columns = this.territory.getColumnCount();
        final int rows = this.territory.getRowCount();
        hamsterGrid.setMinSize(TILE_SIZE * columns + INSET, TILE_SIZE * rows + INSET);
        hamsterGrid.setMaxSize(TILE_SIZE * columns + INSET, TILE_SIZE * rows + INSET);
    }

    private void configureSquareSizedTiles() {
        //        gridPane.getChildren().remove(grid);
        //        grid = new GridPane();
        //        grid.getStyleClass().add("game-grid");

        final int columns = this.territory.getColumnCount();
        final int rows = this.territory.getRowCount();
        for (int i = 0; i < columns; i++) {
            final ColumnConstraints column = new ColumnConstraints();
            column.setPercentWidth(100.0 / columns);
            hamsterGrid.getColumnConstraints().add(column);
        }

        for (int i = 0; i < rows; i++) {
            final RowConstraints row = new RowConstraints(TILE_SIZE);
            row.setPercentHeight(100.0 / rows);
            hamsterGrid.getRowConstraints().add(row);
        }
        hamsterGrid.setAlignment(Pos.CENTER);
        computeNewMinimumSize();
        //        gridPane.getChildren().add(grid);
    }

    protected void connectToTerritory() {
        JavaFXUtil.blockingExecuteOnFXThread(() -> computeNewMinimumSize());
        this.territory.territorySizeProperty().addListener((property, oldValue, newValue) -> {
            JavaFXUtil.blockingExecuteOnFXThread(() -> {
                configureSquareSizedTiles();
                territoryTile = new TileNode[newValue.width][newValue.height];
            });
        });
        this.territory.tilesProperty().addListener(new ListChangeListener<Tile>() {

            @Override
            public void onChanged(final Change<? extends Tile> change) {
                JavaFXUtil.blockingExecuteOnFXThread(() -> {
                    while (change.next()) {
                        if (change.wasAdded()) {
                            change.getAddedSubList().forEach(tile -> {
                                territoryTile[tile.getLocation().getColumn()][tile.getLocation()
                                                                              .getRow()] = new TileNode(GameSceneController.this, tile);
                                hamsterGrid.add(territoryTile[tile.getLocation().getColumn()][tile.getLocation().getRow()],
                                        tile.getLocation().getColumn(), tile.getLocation().getRow());
                            });
                        }
                        if (change.wasRemoved()) {
                            change.getRemoved().forEach(tile -> {
                                territoryTile[tile.getLocation().getColumn()][tile.getLocation().getRow()].dispose();
                                territoryTile[tile.getLocation().getColumn()][tile.getLocation().getRow()] = null;
                                // TODO:
                                // grid.remove(territoryTile[e.getTile().getTileLocation().getColumn()][e.getTile().getTileLocation().getRow()],e.getTile().getTileLocation().getColumn(),e.getTile().getTileLocation().getRow());
                            });
                        }
                    }
                });

            }

        });
    }
}

