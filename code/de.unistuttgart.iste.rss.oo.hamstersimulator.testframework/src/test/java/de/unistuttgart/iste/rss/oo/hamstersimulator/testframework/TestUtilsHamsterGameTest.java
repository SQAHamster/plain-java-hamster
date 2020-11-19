package de.unistuttgart.iste.rss.oo.hamstersimulator.testframework;

import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.HamsterGameViewModel;
import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.ObservableHamster;
import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.ObservableTerritory;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test to test the functionality of the HamsterTestUtils
 */
public class TestUtilsHamsterGameTest {

    /**
     * TestsUtils for all tests
     */
    private HamsterGameTestEnvironment testEnvironment;

    /**
     * Initializes the HamsterGameTestEnvironment
     */
    @BeforeEach
    public void initTestEnvironment() {
        testEnvironment = new HamsterGameTestEnvironment(new TestSimpleHamsterGame());
    }

    /**
     * Tests if the game is actually executed after runGame() and not executed (but initialized) before runGame
     */
    @Test
    public void testExecution() {
        assertEquals(Location.from(0, 0), getDefaultHamsterLocation());
        testEnvironment.runGame();
        assertEquals(Location.from(5, 0), getDefaultHamsterLocation());
    }

    /**
     * Gets the default hamster from the testEnvironment
     * @return the default hamster from testEnvironment, != null
     */
    private ObservableHamster getDefaultHamster() {
        final HamsterGameViewModel viewModel = testEnvironment.getViewModel();
        final ObservableTerritory territory = viewModel.getTerritory();
        return territory.getDefaultHamster();
    }

    /**
     * Gets the current location of the default hamster from testEnvironment
     * @return the location of the default hamster, != null
     */
    private Location getDefaultHamsterLocation() {
        final ObservableHamster defaultHamster = getDefaultHamster();
        final Optional<Location> location = defaultHamster.getCurrentLocation();
        return location.orElseThrow(IllegalStateException::new);
    }
}
