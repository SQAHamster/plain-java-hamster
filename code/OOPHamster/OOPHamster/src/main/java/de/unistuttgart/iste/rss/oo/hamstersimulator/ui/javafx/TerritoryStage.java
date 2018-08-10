package de.unistuttgart.iste.rss.oo.hamstersimulator.ui.javafx;

import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.Territory;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.Tile;
import javafx.beans.binding.DoubleBinding;
import javafx.collections.ListChangeListener;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

class TerritoryStage extends Stage {

    private static final double TILE_SIZE = 40.0;
    private static final double INSET = TILE_SIZE / 2.0;

    private StackPane gridPane;
    private ToolBar toolbar;
    private TerritoryTilePane[][] territoryTile;
    private final Territory territory;
    private GridPane grid;

    public TerritoryStage(final Territory territory) {
        super();
        this.territory = territory;
        prepareStage();

        connectToTerritory();

    }

    protected void connectToTerritory() {
        JavaFXUtil.blockingExecuteOnFXThread(() -> initGamefield());
        this.territory.territorySizeProperty().addListener((property, oldValue, newValue) -> {
            JavaFXUtil.blockingExecuteOnFXThread(() -> {
                configureSquareSizedTiles();
                territoryTile = new TerritoryTilePane[newValue.width][newValue.height];
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
                                                                              .getRow()] = new TerritoryTilePane(tile);
                                grid.add(territoryTile[tile.getLocation().getColumn()][tile.getLocation().getRow()],
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

    public void prepareStage() {
        this.setTitle("Hamster Simulator");
        final BorderPane root = getRootScene();
        final Scene scene = new Scene(root, 300, 250);

        final double offset = this.getHeight() - scene.getHeight();
        final DoubleBinding prop = gridPane.minHeightProperty().add(toolbar.heightProperty()).add(offset);
        this.minHeightProperty().bind(prop);
        this.minWidthProperty().bind(gridPane.minWidthProperty());

        scene.getStylesheets().add("css/game.css");
        this.setScene(scene);
    }

    private BorderPane getRootScene() {
        final BorderPane borderPane = new BorderPane();
        toolbar = new ToolBar(new Button("Start"));
        borderPane.setTop(toolbar);
        grid = new GridPane();
        grid.getStyleClass().add("game-grid");

        gridPane = new StackPane();
        gridPane.setStyle("-fx-background-color: white;");
        gridPane.getChildren().add(grid);
        gridPane.setMinSize(10, 10);

        borderPane.setCenter(gridPane);
        return borderPane;
    }

    private void initGamefield() {
        configureSquareSizedTiles();
    }

    private void computeNewMinimumSize() {
        final int columns = this.territory.getColumnCount();
        final int rows = this.territory.getRowCount();
        gridPane.setMinSize(TILE_SIZE*columns+INSET, TILE_SIZE*rows+INSET);
        gridPane.setMaxSize(TILE_SIZE*columns+INSET, TILE_SIZE*rows+INSET);
    }

    private void configureSquareSizedTiles() {
        gridPane.getChildren().remove(grid);
        grid = new GridPane();
        grid.getStyleClass().add("game-grid");

        final int columns = this.territory.getColumnCount();
        final int rows = this.territory.getRowCount();
        for(int i = 0; i < columns; i++) {
            final ColumnConstraints column = new ColumnConstraints();
            column.setPercentWidth(100.0 / columns);
            grid.getColumnConstraints().add(column);
        }

        for(int i = 0; i < rows; i++) {
            final RowConstraints row = new RowConstraints(TILE_SIZE);
            row.setPercentHeight(100.0 / rows);
            grid.getRowConstraints().add(row);
        }
        grid.setAlignment(Pos.CENTER);
        computeNewMinimumSize();
        gridPane.getChildren().add(grid);
    }
}
