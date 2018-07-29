package de.unistuttgart.iste.rss.oo.hamstersimulator.legacy.ui;

import java.util.HashMap;
import java.util.Map;

import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Direction;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;
import de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.Hamster;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class TerritoryTilePane extends Pane {

    private final Location location;
    private static final Image hamsterImage = new Image("Hamster24.png");
    private static final Image wallImage = new Image("Wall32.png", 40, 40, true, true);

    private final Map<Hamster,ImageView> hamsterImages = new HashMap<>();
    private final Map<Integer,Image> cornImages = new HashMap<>();
    private final ImageView grainView;

    public TerritoryTilePane(final Location location) {
        super();
        this.location = location;
        configureStyle();
        grainView = new ImageView();
        getChildren().add(grainView);
        loadCornImages();
    }

    private void loadCornImages() {
        for (int i = 1; i < 13; i++) {
            cornImages.put(i, new Image(i+"Corn32.png"));
        }
    }

    private void configureStyle() {
        getStyleClass().add("game-grid-cell");
        if (location.getColumn() == 0) {
            getStyleClass().add("first-column");
        }
        if (location.getRow() == 0) {
            getStyleClass().add("first-row");
        }
    }

    public void showHamster(final Hamster hamster) {
        ImageView view;
        if (!hamsterImages.containsKey(hamster)) {
            view = new ImageView(hamsterImage);
            view.resizeRelocate(8, 8, 24, 24);
            hamsterImages.put(hamster, view);
            getChildren().add(view);
        } else {
            view = hamsterImages.get(hamster);
        }
        view.setRotate(getRotationForDirection(hamster.getDirection()));
    }

    public void showWall() {
        final ImageView view = new ImageView(wallImage);
        getChildren().add(view);
    }

    public void removeWall() {
        getChildren().remove(0);
    }

    public void removeHamster(final Hamster hamster) {
        final ImageView view = hamsterImages.get(hamster);
        getChildren().remove(view);
        hamsterImages.remove(hamster);
    }

    public void setGrains(final int count) {
        if (count < 1) {
            grainView.setVisible(false);
        } else {
            grainView.setVisible(true);
            grainView.setImage(cornImages.get(count < 12 ? count : 12 ));
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
}
