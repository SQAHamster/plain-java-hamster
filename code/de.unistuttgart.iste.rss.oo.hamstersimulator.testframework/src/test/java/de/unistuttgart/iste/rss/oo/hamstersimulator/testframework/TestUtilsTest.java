package de.unistuttgart.iste.rss.oo.hamstersimulator.testframework;

import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestUtilsTest {
    @Test
    public void testExecution() {
        final TestUtils utils = new TestUtils(new TestSimpleHamsterGame());
        assertEquals(Location.from(0, 0),
                utils.getViewModel().getTerritory().getDefaultHamster().getCurrentLocation().orElseThrow());
        utils.runGame();
        assertEquals(Location.from(5, 0),
                utils.getViewModel().getTerritory().getDefaultHamster().getCurrentLocation().orElseThrow());
    }
}
