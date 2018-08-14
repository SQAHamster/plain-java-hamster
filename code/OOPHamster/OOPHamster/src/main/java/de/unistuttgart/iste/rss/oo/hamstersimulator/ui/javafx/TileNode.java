package de.unistuttgart.iste.rss.oo.hamstersimulator.ui.javafx;

import java.util.HashMap;
import java.util.Map;

import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Direction;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.Hamster;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.Tile;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.TileContent;
import javafx.beans.binding.Bindings;
import javafx.collections.ListChangeListener;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class TileNode extends Pane {

    private static final Image hamsterImage = new Image("images/Hamster24.png");
    private static final Image wallImage = new Image("images/Wall32.png", 39, 39, true, true);
    private static final Map<Integer, Image> cornImages = new HashMap<>();
    private static final Color[] hamsterColors = new Color[] {
            Color.BLUE,
            Color.GREEN,
            Color.YELLOW,
            Color.PINK,
            Color.MAGENTA,
            Color.RED
    };

    static {
        loadCornImages();
    }

    private static void loadCornImages() {
        for (int i = 1; i < 13; i++) {
            cornImages.put(i, new Image("images/"+ i + "Corn32.png"));
        }
    }

    private final HamsterTerritoryGrid parent;
    private final Group imageGroup;
    private ImageView wallView;
    private ImageView grainView;
    private final Map<Hamster, ImageView> hamsterImageViews = new HashMap<>();
    private final Tile tile;

    private final ListChangeListener<TileContent> tileListener = new ListChangeListener<TileContent>(){

        @Override
        public void onChanged(final Change<? extends TileContent> change) {
            while(change.next()) {
                if (change.wasAdded()) {
                    change.getAddedSubList().forEach(tileContent -> addHamster((Hamster)tileContent));
                }
                if (change.wasRemoved()) {
                    change.getRemoved().forEach(tileContent -> removeHamster((Hamster)tileContent));
                }
            }
        }

    };


    TileNode(final HamsterTerritoryGrid hamsterTerritoryGrid, final Tile tile) {
        super();

        this.tile = tile;
        this.parent = hamsterTerritoryGrid;

        configureStyle();
        this.imageGroup = new Group();
        this.getChildren().add(this.imageGroup);
        configureWallImageView();
        configureGrainImageView();

        addHamsterListListener();
    }

    private void configureGrainImageView() {
        this.grainView = new ImageView();
        this.imageGroup.getChildren().add(grainView);
        grainView.visibleProperty().bind(Bindings.createBooleanBinding(() -> tile.getGrainCount() > 0, tile.grainCountProperty()));
        grainView.imageProperty().bind(Bindings.createObjectBinding(() -> tile.getGrainCount() <= 12 ? cornImages.get(tile.getGrainCount()) : cornImages.get(12), tile.grainCountProperty()));
    }

    private void configureWallImageView() {
        this.wallView = new ImageView(wallImage);
        this.imageGroup.getChildren().add(wallView);
        wallView.visibleProperty().bind(tile.isBlockedProperty());
    }

    private void addHamster(final Hamster hamster) {
        if (!hamsterImageViews.containsKey(hamster)) {
            final Image coloredHamsterImage = getColoredHamsterImage(hamster);
            final ImageView view = new ImageView(coloredHamsterImage);
            view.resizeRelocate(8, 8, 24, 24);
            hamsterImageViews.put(hamster, view);
            view.rotateProperty().bind(Bindings.createDoubleBinding(() -> getRotationForDirection(hamster.getDirection()), hamster.directionProperty()));
            JavaFXUtil.blockingExecuteOnFXThread(() -> {
                this.imageGroup.getChildren().add(hamsterImageViews.get(hamster));
            });
        }
    }

    private void removeHamster(final Hamster hamster) {
        final ImageView view = hamsterImageViews.remove(hamster);
        JavaFXUtil.blockingExecuteOnFXThread(() -> this.imageGroup.getChildren().remove(view));
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

    private Image getColoredHamsterImage(final Hamster hamster) {
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
