package de.unistuttgart.iste.oo.hamstersimulator.testFramework;

import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.InputInterface;
import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.ObservableTile;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Direction;
import de.unistuttgart.iste.rss.oo.hamstersimulator.testFramework.HamsterGameResolver;
import de.unistuttgart.iste.rss.oo.hamstersimulator.testFramework.HamsterTest;
import de.unistuttgart.iste.rss.oo.hamstersimulator.testFramework.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@HamsterTest(game = "de.unistuttgart.iste.rss.oo.hamstersimulator.external.model.SimpleHamsterGame")
@ExtendWith(HamsterGameResolver.class)
public class ExampleTest {

    @Test
    public void test(final TestUtils utils) {
        utils.getViewModel().addInputInterface(new InputInterface() {
            @Override
            public Optional<Integer> readInteger(final String message) {
                return Optional.of(0);
            }

            @Override
            public Optional<String> readString(final String message) {
                return Optional.of("Bla");
            }

            @Override
            public void showAlert(final Throwable t) {
                fail(t);
            }

            @Override
            public void abort() {

            }
        });

        final ObservableTile t = null;

        assertEquals(Direction.EAST, utils.getViewModel().getTerritory().defaultHamsterProperty().get().directionProperty().get());

        utils.runGame();

        assertEquals(Direction.NORTH, utils.getViewModel().getTerritory().defaultHamsterProperty().get().directionProperty().get());
        //assertEquals(new Location(1, 3), utils.getViewModel().getTerritory().defaultHamsterProperty().get().currentTileProperty().get().get().getLocation());
    }
}