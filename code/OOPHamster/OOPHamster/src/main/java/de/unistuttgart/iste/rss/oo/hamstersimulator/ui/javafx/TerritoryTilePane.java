package de.unistuttgart.iste.rss.oo.hamstersimulator.ui.javafx;

import java.util.HashMap;
import java.util.Map;

import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Direction;
import de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.Hamster;
import de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.HamsterChangedDirectionEvent;
import de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.HamsterStateChangedEvent;
import de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.HamsterStateListener;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.Grain;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.Tile;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.TileContentAddedEvent;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.TileContentRemovedEvent;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.TileListener;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.Wall;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class TerritoryTilePane extends Pane {

    private static final Image hamsterImage = new Image("Hamster24.png");
    private static final Image wallImage = new Image("Wall32.png", 40, 40, true, true);
    private static final Map<Integer, Image> cornImages = new HashMap<>();

    static {
        loadCornImages();
    }

    private final Tile tile;
    private final Map<Hamster, ImageView> hamsterImageViews = new HashMap<>();
    private final ImageView grainView;
    private final ImageView wallView;

    private final HamsterStateListener hamsterListener = new HamsterStateListener() {

        @Override
        public void onStateChanged(final HamsterStateChangedEvent e) {
            if (e instanceof HamsterChangedDirectionEvent) {
                showHamster(e.getHamster());
            }
        }

    };

    TerritoryTilePane(final Tile tile) {
        super();
        this.tile = tile;
        this.grainView = new ImageView();
        getChildren().add(grainView);
        this.wallView = new ImageView(wallImage);
        configureStyle();
        initViewFromTile();
        addTileListener();
    }

    void showHamster(final Hamster hamster) {
        JavaFXUtil.blockingExecuteOnFXThread(() -> {
            ImageView view;
            if (!hamsterImageViews.containsKey(hamster)) {
                view = new ImageView(hamsterImage);
                view.resizeRelocate(8, 8, 24, 24);
                hamsterImageViews.put(hamster, view);
                hamster.addHamsterStateListener(hamsterListener);
                getChildren().add(view);
            } else {
                view = hamsterImageViews.get(hamster);
            }
            view.setRotate(getRotationForDirection(hamster.getDirection()));
        });
    }

    void removeHamster(final Hamster hamster) {
        final ImageView view = hamsterImageViews.remove(hamster);
        JavaFXUtil.blockingExecuteOnFXThread(() -> getChildren().remove(view));
        hamster.removeHamsterStateListener(hamsterListener);
    }

    void showWall() {
        JavaFXUtil.blockingExecuteOnFXThread(() -> getChildren().add(this.wallView));
    }

    void removeWall() {
        JavaFXUtil.blockingExecuteOnFXThread(() -> getChildren().remove(this.wallView));
    }

    public void updateGrains() {
        final int count = tile.countObjectsOfType(Grain.class);
        if (count < 1) {
            grainView.setVisible(false);
        } else {
            grainView.setVisible(true);
            grainView.setImage(cornImages.get(count < 12 ? count : 12));
        }
    }

    private double getRotationForDirection(final Direction direction) {
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

    private static void loadCornImages() {
        for (int i = 1; i < 13; i++) {
            cornImages.put(i, new Image(i + "Corn32.png"));
        }
    }

    private void configureStyle() {
        getStyleClass().add("game-grid-cell");
        if (tile.getTileLocation().getColumn() == 0) {
            getStyleClass().add("first-column");
        }
        if (tile.getTileLocation().getRow() == 0) {
            getStyleClass().add("first-row");
        }
    }

    private void initViewFromTile() {
        if (!tile.canEnter()) {
            this.showWall();
        }
        updateGrains();
        if (tile.countObjectsOfType(Hamster.class) > 0) {
            this.showHamster(tile.getAnyContentOfType(Hamster.class));
        }
    }

    private void addTileListener() {
        this.tile.addTileListener(new TileListener() {

            @Override
            public void contentItemRemoved(final TileContentRemovedEvent e) {
                if (e.getRemovedContent() instanceof Wall) {
                    removeWall();
                } else if (e.getRemovedContent() instanceof Hamster) {
                    removeHamster((Hamster) e.getRemovedContent());
                } else if (e.getRemovedContent() instanceof Grain) {
                    updateGrains();
                }
            }

            @Override
            public void contentItemAdded(final TileContentAddedEvent e) {
                if (e.getNewContent() instanceof Wall) {
                    showWall();
                } else if (e.getNewContent() instanceof Hamster) {
                    showHamster((Hamster) e.getNewContent());
                } else if (e.getNewContent() instanceof Grain) {
                    updateGrains();
                }
            }
        });

    }
}
