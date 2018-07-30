package de.unistuttgart.iste.rss.oo.hamstersimulator.ui.javafx;

import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.Territory;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.TerritoryListener;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.TerritoryResizedEvent;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.TileAddedEvent;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.TileRemovedEvent;
import javafx.application.Application;
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

public class JavaFXUI extends Application {

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
        JavaFXUtil.blockingExecuteOnFXThread(() -> initGamefield());
        this.territory.addTerritoryListener(territoryListener);
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

    public static void start() {
        new Thread(()->Application.launch(JavaFXUI.class)).start();
    }

    private final TerritoryListener territoryListener = new TerritoryListener() {

        @Override
        public void tileRemoved(final TileRemovedEvent e) {
            JavaFXUtil.blockingExecuteOnFXThread(() -> {
                territoryTile[e.getTile().getTileLocation().getColumn()][e.getTile().getTileLocation().getRow()].dispose();
                territoryTile[e.getTile().getTileLocation().getColumn()][e.getTile().getTileLocation().getRow()] = null;
                // TODO: grid.remove(territoryTile[e.getTile().getTileLocation().getColumn()][e.getTile().getTileLocation().getRow()],e.getTile().getTileLocation().getColumn(),e.getTile().getTileLocation().getRow());
            });
        }

        @Override
        public void tileAdded(final TileAddedEvent e) {
            JavaFXUtil.blockingExecuteOnFXThread(() -> {
                territoryTile[e.getTile().getTileLocation().getColumn()][e.getTile().getTileLocation().getRow()] = new TerritoryTilePane(e.getTile());
                grid.add(territoryTile[e.getTile().getTileLocation().getColumn()][e.getTile().getTileLocation().getRow()],e.getTile().getTileLocation().getColumn(),e.getTile().getTileLocation().getRow());
            });
        }

        @Override
        public void territoryResized(final TerritoryResizedEvent e) {
            JavaFXUtil.blockingExecuteOnFXThread(() -> {
                configureSquareSizedTiles();
                territoryTile = new TerritoryTilePane[e.getColumnCount()][e.getRowCount()];
            });
        }
    };
}
