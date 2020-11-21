package de.unistuttgart.iste.rss.oo.hamstersimulator.testframework;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.HamsterGameViewModel;
import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.ObservableHamster;
import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.ObservableTerritory;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;

/**
 * Test to test the functionality of the HamsterTestUtils.
 */
public final class HamsterGameTestEnvironmentTest {

    /**
     * Expected location of paule after the game.
     */
    private static final Location FINAL_HAMSTER_LOCATION = Location.from(5, 0);

    /**
     * HamsterGameTestEnvironment for all tests.
     */
    private HamsterGameTestEnvironment testEnvironment;

    /**
     * The default hamster of {@link TestSimpleHamsterGame}.
     */
    private ObservableHamster paule;

    /**
     * Initializes the HamsterGameTestEnvironment with the game defined in {@link TestSimpleHamsterGame}.
     */
    @BeforeEach
    public void initTestEnvironment() {
        testEnvironment = new HamsterGameTestEnvironment(new TestSimpleHamsterGame());
        paule = getDefaultHamster();
    }

    /**
     * Tests if the game is actually executed after runGame() and not executed (but initialized) before runGame.
     */
    @Test
    public void testExecution() {
        assertEquals(Location.ORIGIN, getDefaultHamsterLocation());
        testEnvironment.runGame();
        assertEquals(FINAL_HAMSTER_LOCATION, getDefaultHamsterLocation());
    }

    /**
     * Gets the default hamster from the testEnvironment.
     * @return the default hamster from testEnvironment, != null
     */
    private ObservableHamster getDefaultHamster() {
        final HamsterGameViewModel viewModel = testEnvironment.getViewModel();
        final ObservableTerritory territory = viewModel.getTerritory();
        return territory.getDefaultHamster();
    }

    /**
     * Gets the current location of the default hamster from testEnvironment.
     * @return the location of the default hamster, != null
     */
    private Location getDefaultHamsterLocation() {
        final Optional<Location> location = paule.getCurrentLocation();
        return location.orElseThrow(IllegalStateException::new);
    }
}
