package de.unistuttgart.iste.sqa.oo.hamstersimulator.main.tests;

import de.unistuttgart.iste.sqa.oo.hamstersimulator.adapter.observables.ObservableHamster;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.adapter.observables.ObservableTerritory;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.external.model.Hamster;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Class to test the observable features of ObservableHamster
 */
public class ObservableHamsterTest extends BaseHamsterGameTest {

    /**
     * Tests that the default hamster turns 5 times and does not move at all
     */
    @Test
    public void testTurnFiveTimes() {
        final ObservableTerritory observableTerritory = getViewModel().getTerritory();
        final ObservableHamster observableHamster = observableTerritory.getDefaultHamster();
        observableHamster.currentTileProperty().addListener((property, oldTile, newTile) -> {
            fail("not allowed to move");
        });
        final AtomicInteger amountTurns = new AtomicInteger(0);
        observableHamster.directionProperty().addListener((property, oldDirection, newDirection) -> {
            amountTurns.incrementAndGet();
        });

        final Hamster hamster = game.getTerritory().getDefaultHamster();
        for (int i = 0; i < 5; i++) {
            hamster.turnLeft();
        }

        assertEquals(5, amountTurns.get());
    }

    /**
     * Tests that the default hamster moves 3 times and does not turn at all
     */
    @Test
    public void testMoveThreeTimes() {
        final ObservableTerritory observableTerritory = getViewModel().getTerritory();
        final ObservableHamster observableHamster = observableTerritory.getDefaultHamster();
        final AtomicInteger amountMoves = new AtomicInteger(0);
        observableHamster.currentTileProperty().addListener((property, oldTile, newTile) -> {
            amountMoves.incrementAndGet();
        });
        observableHamster.directionProperty().addListener((property, oldDirection, newDirection) -> {
            fail("not allowed to turn");
        });

        final Hamster hamster = game.getTerritory().getDefaultHamster();
        for (int i = 0; i < 3; i++) {
            hamster.move();
        }

        assertEquals(3, amountMoves.get());
    }

    /**
     * Tests that the default hamster (moves and then turns) times 4
     */
    @Test
    public void testMoveTurnFourTimes() {
        final ObservableTerritory observableTerritory = getViewModel().getTerritory();
        final ObservableHamster observableHamster = observableTerritory.getDefaultHamster();

        final Hamster hamster = game.getTerritory().getDefaultHamster();

        final AtomicInteger amountMoves = new AtomicInteger(0);
        checkHamsterMovesAndTurns(observableHamster, amountMoves);

        for (int i = 0; i < 4; i++) {
            hamster.move();
            hamster.turnLeft();
        }

        assertEquals(4, amountMoves.get());
    }

    /**
     * checks that the hamster moves and then turns
     * @param observableHamster the hamster to observe, must be != null
     * @param amountMoves the move counter, must be != null
     */
    private void checkHamsterMovesAndTurns(ObservableHamster observableHamster, AtomicInteger amountMoves) {
        final AtomicBoolean didLastMove = new AtomicBoolean(false);
        observableHamster.currentTileProperty().addListener((property, oldTile, newTile) -> {
            if (didLastMove.get()) {
                fail("did not turn");
            } else {
                didLastMove.set(true);
            }
            amountMoves.incrementAndGet();
        });
        observableHamster.directionProperty().addListener((property, oldDirection, newDirection) -> {
            if (!didLastMove.get()) {
                fail("did not move");
            } else {
                didLastMove.set(false);
            }
        });
    }

}
