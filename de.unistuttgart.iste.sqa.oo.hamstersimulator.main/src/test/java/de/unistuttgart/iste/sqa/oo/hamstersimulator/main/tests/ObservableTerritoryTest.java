package de.unistuttgart.iste.sqa.oo.hamstersimulator.main.tests;

import de.unistuttgart.iste.sqa.oo.hamstersimulator.adapter.observables.ObservableHamster;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.adapter.observables.ObservableTerritory;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.adapter.observables.ObservableTile;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.adapter.observables.ObservableTileContent;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.datatypes.Direction;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.datatypes.Location;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.external.model.Hamster;
import javafx.collections.ListChangeListener;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class to test the observable features of ObservableTerritory
 */
public class ObservableTerritoryTest extends BaseHamsterGameTest {

    /**
     * Tests that four additional hamsters were added
     */
    @Test
    public void testAddedFourHamsters() {
        final ObservableTerritory observableTerritory = getViewModel().getTerritory();
        final AtomicInteger amountAdded = new AtomicInteger(0);
        observableTerritory.hamstersProperty().addListener((ListChangeListener<ObservableHamster>) change -> {
            while (change.next()) {
                amountAdded.addAndGet(change.getAddedSize());
            }
        });

        for (int i = 0; i < 4; i++) {
            new Hamster(game.getTerritory(), Location.from(0, 0), Direction.SOUTH, 0);
        }

        assertEquals(4, amountAdded.get());
    }

    /**
     * Tests that the bottom left hamster moves to the bottom right corner,
     * and only moves at the edge of the territory
     */
    @Test
    public void testMoveBottomRight() {
        final ObservableTerritory observableTerritory = getViewModel().getTerritory();
        observableTerritory.tilesProperty().forEach(tile -> tile.hamstersProperty()
                .addListener((ListChangeListener<ObservableTileContent>) change -> {
                    while (change.next()) {
                        if (change.wasAdded()) {
                            final Location location = tile.getLocation();
                            if ((location.getColumn() != 0) && (location.getRow() != 9)) {
                                fail("hamster did not move at the left or bottom edge");
                            }
                        }
                    }
                }));

        final Hamster hamster = game.getTerritory().getDefaultHamster();
        while (hamster.frontIsClear()) {
            hamster.move();
        }
        hamster.turnLeft();
        while (hamster.frontIsClear()) {
            hamster.move();
        }

        assertEquals(Location.from(9, 9), hamster.getLocation());
    }

    /**
     * Tests that the default hamster visits each tile of the territory
     */
    @Test
    public void testVisitedEachTile() {
        final ObservableTerritory observableTerritory = getViewModel().getTerritory();
        final List<ObservableTile> tilesToVisit = new ArrayList<>(observableTerritory.tilesProperty().get());

        //remove start point
        tilesToVisit.remove(observableTerritory.getTileAt(Location.ORIGIN));

        observableTerritory.tilesProperty().forEach(tile -> tile.hamstersProperty()
                .addListener((ListChangeListener<ObservableTileContent>) change -> {
                    while (change.next()) {
                        if (change.wasAdded()) {
                            tilesToVisit.remove(tile);
                        }
                    }
                }));

        final Hamster hamster = game.getTerritory().getDefaultHamster();

        // moves the hamster in a spiral to the center
        visitAllTiles(hamster);
        assertTrue(tilesToVisit.isEmpty());
    }

    /**
     * moves the hamster in a spiral to the center
     * assumes that the hamster is at 0,0 and that the territory is 10 by 10
     * @param hamster the hamster to move
     */
    private void visitAllTiles(final Hamster hamster) {
        for (int level = 9; level > 0; level -= 2) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < level; j++) {
                    hamster.move();
                }
                hamster.turnLeft();
            }
            for (int j = 0; j < (level - 1); j++) {
                hamster.move();
            }
            hamster.turnLeft();
            hamster.move();
        }
    }
}
