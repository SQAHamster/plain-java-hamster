package de.unistuttgart.iste.rss.oo.hamstersimulator.ui.javafx;

import java.util.HashMap;
import java.util.Map;

import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Direction;
import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.ObservableHamster;
import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.ObservableTile;
import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.ObservableTileContent;
import javafx.beans.binding.Bindings;
import javafx.collections.ListChangeListener;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class TileNode extends StackPane {

    private static final Image hamsterImage = new Image(
            "de/unistuttgart/iste/rss/oo/hamstersimulator/ui/ressources/images/Hamster24.png");
    private static final Image wallImage = new Image(
            "de/unistuttgart/iste/rss/oo/hamstersimulator/ui/ressources/images/Wall32.png", 39, 39, true, true);
    private static final Map<Integer, Image> cornImages = new HashMap<>();
    static final Color[] hamsterColors = new Color[] {
            Color.BLUE,
            Color.GREEN,
            Color.YELLOW,
            Color.PINK,
            Color.MAGENTA,
            Color.RED
    };
    private final static Image PLUS_12_CORN_IMAGE = new Image(
            "de/unistuttgart/iste/rss/oo/hamstersimulator/ui/ressources/images/12PlusCorn32.png");

    static {
        loadCornImages();
    }

    private static void loadCornImages() {
        for (int i = 1; i < 13; i++) {
            cornImages.put(i,
                    new Image("de/unistuttgart/iste/rss/oo/hamstersimulator/ui/ressources/images/" + i + "Corn32.png"));
        } 
    }

    private final HamsterTerritoryGrid parent;
    private ImageView wallView;
    private ImageView grainView;
    private final Map<ObservableHamster, ImageView> hamsterImageViews = new HashMap<>();
    final ObservableTile tile;

    private final ListChangeListener<ObservableTileContent> tileListener = new ListChangeListener<ObservableTileContent>(){

        @Override
        public void onChanged(final Change<? extends ObservableTileContent> change) {
            while(change.next()) {
                if (change.wasAdded()) {
                    change.getAddedSubList().forEach(tileContent -> addHamster((ObservableHamster) tileContent));
                }
                if (change.wasRemoved()) {
                    change.getRemoved().forEach(tileContent -> removeHamster((ObservableHamster)tileContent));
                }
            }
        }

    };


    TileNode(final HamsterTerritoryGrid hamsterTerritoryGrid, final ObservableTile tile) {
        super();

        this.tile = tile;
        this.parent = hamsterTerritoryGrid;

        configureStyle();
        configureWallImageView();
        configureGrainImageView();
        tile.hamstersProperty().forEach(hamster -> addHamster((ObservableHamster) hamster));
        addHamsterListListener();
    }

    private void configureGrainImageView() {
        this.grainView = new ImageView();
        grainView.visibleProperty().bind(Bindings.createBooleanBinding(() -> tile.grainCountProperty().get() > 0, tile.grainCountProperty()));
        grainView.imageProperty().bind(Bindings.createObjectBinding(() -> tile.grainCountProperty().get() <= 12 ? cornImages.get(tile.grainCountProperty().get()) : PLUS_12_CORN_IMAGE, tile.grainCountProperty()));
        grainView.fitHeightProperty().bind(this.heightProperty());
        grainView.fitWidthProperty().bind(this.widthProperty());
        grainView.setPreserveRatio(true);
        this.getChildren().add(grainView);
    }

    private void configureWallImageView() {
        this.wallView = new ImageView(wallImage);
        wallView.visibleProperty().bind(tile.isBlockedProperty());
        wallView.fitHeightProperty().bind(this.heightProperty());
        wallView.fitWidthProperty().bind(this.widthProperty());
        wallView.setPreserveRatio(false);
        this.getChildren().add(wallView);
    }

    private void addHamster(final ObservableHamster hamster) {
        if (!hamsterImageViews.containsKey(hamster)) {
            final Image coloredHamsterImage = getColoredHamsterImage(hamster);
            final ImageView view = new ImageView(coloredHamsterImage);
            view.fitHeightProperty().bind(this.heightProperty().multiply(0.7));
            view.fitWidthProperty().bind(this.widthProperty().multiply(0.7));
            view.setPreserveRatio(true);
            hamsterImageViews.put(hamster, view);
            view.rotateProperty().bind(Bindings.createDoubleBinding(() -> getRotationForDirection(hamster.directionProperty().get()), hamster.directionProperty()));
            JavaFXUtil.blockingExecuteOnFXThread(() -> {
                this.getChildren().add(hamsterImageViews.get(hamster));
            });
        }
    }

    private void removeHamster(final ObservableHamster hamster) {
        final ImageView view = hamsterImageViews.remove(hamster);
        JavaFXUtil.blockingExecuteOnFXThread(() -> this.getChildren().remove(view));
    }

    private double getRotationForDirection(final Direction direction) {
        assert direction != null;

        switch (direction) {
        case EAST:
            return 0;
        case SOUTH:
            return 90;
        case WEST:
            return 180;
        case NORTH:
            return 270;
        }
        throw new RuntimeException("Invalid direction!");
    }

    private void configureStyle() {
        this.getStyleClass().add("game-grid-cell");
        if (tile.getLocation().getColumn() == 0) {
            this.getStyleClass().add("first-column");
        }
        if (tile.getLocation().getRow() == 0) {
            this.getStyleClass().add("first-row");
        }
    }

    private Image getColoredHamsterImage(final ObservableHamster hamster) {
        int colorIndex;
        if (parent.hamsterToColorPos.containsKey(hamster)) {
            colorIndex = parent.hamsterToColorPos.get(hamster);
        } else {
            colorIndex = getIndexOfColorForHamster();
            assert !parent.hamsterToColorPos.containsValue(colorIndex);
            parent.hamsterToColorPos.put(hamster, colorIndex);
        }
        return JavaFXUtil.changeColor(hamsterImage, hamsterColors[colorIndex]);
    }

    private int getIndexOfColorForHamster() {
        for (int i = 0; i < hamsterColors.length; i++) {
            if (!parent.hamsterToColorPos.containsValue(i)) {
                return i;
            }
        }
        throw new RuntimeException("No more colors for hamster available.");
    }

    private void addHamsterListListener() {
        this.tile.hamstersProperty().addListener(this.tileListener);
    }

    public void dispose() {
        this.tile.hamstersProperty().removeListener(this.tileListener);
    }
}
