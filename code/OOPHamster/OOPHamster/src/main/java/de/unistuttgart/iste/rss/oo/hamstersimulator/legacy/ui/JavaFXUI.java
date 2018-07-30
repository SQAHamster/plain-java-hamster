package de.unistuttgart.iste.rss.oo.hamstersimulator.legacy.ui;

import java.util.concurrent.CountDownLatch;

import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;
import de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.Hamster;
import de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.HamsterChangedDirectionEvent;
import de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.HamsterGrainAddedEvent;
import de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.HamsterGrainDeletedEvent;
import de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.HamsterMovedEvent;
import de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.HamsterStateChangedEvent;
import de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.HamsterStateListener;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.Grain;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.Territory;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.Tile;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.DoubleBinding;
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

public class JavaFXUI extends Application implements HamsterStateListener {

    private static final double TILE_SIZE = 40.0;
    private static final double INSET = TILE_SIZE / 2.0;

    private StackPane gridPane;
    private ToolBar toolbar;
    private TerritoryTilePane[][] territoryTile;
    private Territory territory;
    private GridPane grid;
    public static JavaFXUI mySingleton = null;

    public static JavaFXUI getSingleton() {
        while (mySingleton == null) {
            try {
                Thread.sleep(100);
            } catch (final InterruptedException e) { }
        }
        return mySingleton;
    }

    @Override
    public void onStateChanged(final HamsterStateChangedEvent event) {
        // queue on JavaFX thread and wait for completion
        final CountDownLatch doneLatch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                if (event instanceof HamsterMovedEvent) {
                    final HamsterMovedEvent hme = (HamsterMovedEvent) event;
                    final Location oldLocation = hme.getOldTile().get().getTileLocation();
                    final Location newLocation = hme.getNewTile().get().getTileLocation();
                    territoryTile[oldLocation.getColumn()][oldLocation.getRow()].removeHamster(hme.getHamster());
                    territoryTile[newLocation.getColumn()][newLocation.getRow()].showHamster(hme.getHamster());
                }
                if (event instanceof HamsterChangedDirectionEvent) {
                    final HamsterChangedDirectionEvent hme = (HamsterChangedDirectionEvent) event;
                    final Location location = hme.getHamster().getCurrentPosition().get();
                    territoryTile[location.getColumn()][location.getRow()].showHamster(hme.getHamster());
                }
                if (event instanceof HamsterGrainAddedEvent || event instanceof HamsterGrainDeletedEvent) {
                    final HamsterStateChangedEvent hme = event;
                    final Location location = hme.getHamster().getCurrentPosition().get();
                    final int count = this.territory.getTileAt(location).countObjectsOfType(Grain.class);
                    territoryTile[location.getColumn()][location.getRow()].setGrains(count);
                }
            } finally {
                doneLatch.countDown();
            }
        });

        try {
            doneLatch.await();
        } catch (final InterruptedException ex) { }
    }

    @Override
    public void start(final Stage primaryStage) throws Exception {
        primaryStage.setTitle("Hamster Simulator");
        final BorderPane root = getRootScene();
        final Scene scene = new Scene(root, 300, 250);
        primaryStage.setScene(scene);

        scene.getStylesheets().add("game.css");
        primaryStage.show();

        final double offset = primaryStage.getHeight() - scene.getHeight();
        final DoubleBinding prop = gridPane.minHeightProperty().add(toolbar.heightProperty()).add(offset);
        primaryStage.minHeightProperty().bind(prop);
        primaryStage.minWidthProperty().bind(gridPane.minWidthProperty());

        mySingleton = this;
    }

    public void init(final Territory territory) {
        this.territory = territory;
        final CountDownLatch doneLatch = new CountDownLatch(1);
        Platform.runLater(() -> {
            initGamefield();
            doneLatch.countDown();
        });
        try {
            doneLatch.await();
        } catch (final InterruptedException e) {}
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
        initTileViewsFromTerritory();
        computeNewMinimumSize();
    }

    private void computeNewMinimumSize() {
        final int columns = this.territory.getColumnCount();
        final int rows = this.territory.getRowCount();
        gridPane.setMinSize(TILE_SIZE*columns+INSET, TILE_SIZE*rows+INSET);
        gridPane.setMaxSize(TILE_SIZE*columns+INSET, TILE_SIZE*rows+INSET);
    }

    private void configureSquareSizedTiles() {
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
    }

    private void initTileViewsFromTerritory() {
        final int columns = this.territory.getColumnCount();
        final int rows = this.territory.getRowCount();
        territoryTile = new TerritoryTilePane[columns][rows];
        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows; j++) {
                territoryTile[i][j] = new TerritoryTilePane(new Location(j, i));
                grid.add(territoryTile[i][j], i, j);
                final Tile tile = territory.getTileAt(new Location(j, i));
                if (!tile.canEnter()) {
                    territoryTile[i][j].showWall();
                }
                territoryTile[i][j].setGrains(tile.countObjectsOfType(Grain.class));
                if (tile.countObjectsOfType(Hamster.class) > 0) {
                    territoryTile[i][j].showHamster(tile.getAnyContentOfType(Hamster.class));
                }
            }
        }
    }

    public static void start() {
        new Thread(()->Application.launch(JavaFXUI.class)).start();
    }

}
