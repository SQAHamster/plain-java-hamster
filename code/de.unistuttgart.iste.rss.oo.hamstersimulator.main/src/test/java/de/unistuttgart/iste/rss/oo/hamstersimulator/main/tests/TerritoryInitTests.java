package de.unistuttgart.iste.rss.oo.hamstersimulator.main.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Direction;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Size;
import de.unistuttgart.iste.rss.oo.hamstersimulator.external.model.Hamster;
import de.unistuttgart.iste.rss.oo.hamstersimulator.external.model.HamsterGame;
import de.unistuttgart.iste.rss.oo.hamstersimulator.external.model.TerritoryBuilder;

/**
 * Simple tests for the hamster API.
 * @author Steffen Becker
 *
 */
@TestInstance(Lifecycle.PER_CLASS)
public class TerritoryInitTests {

    /**
     * Size used in this test for both, colums and rows.
     */
    private static final int TERRITORY_SIZE = 4;


    /**
     * Example territory.
     */
    private final String exampleTerritory =
            "4\n"
            + "4\n"
            + ">   \n"
            + "*   \n"
            + "    \n"
            + "    \n"
            + "0\n"
            + "1\n"
            + "0\n";


    /**
     * Field containing the hamster game used in tests.
     */
    private HamsterGame game;

    /**
     * During a test, this contains a reference to the default hamster.
     */
    private Hamster paule;

    /**
     * Initialize a game and its UI.
     */
    @BeforeAll
    public void createHamsterGame() {
        game = new HamsterGame();
    }

    /**
     * Before each test, load the default territory.
     */
    @BeforeEach
    public void initializeTest() {
        game.initialize(getClass().getResourceAsStream("/_territories/example02.ter"));
        paule = game.getTerritory().getDefaultHamster();
    }

    /**
     * Test loading a territory from an Input Stream.
     */
    @Test
    public void testLoadFromInputStream() {
        game.initialize(exampleTerritory);
        paule = game.getTerritory().getDefaultHamster();
        assertEquals(game.getTerritory().getTerritorySize(), new Size(TERRITORY_SIZE, TERRITORY_SIZE));
        assertEquals(paule.getLocation(), Location.from(0, 0));
        assertEquals(game.getTerritory().getTotalGrainCount(), 1);
    }

    /**
     * Test creating a territory via API.
     */
    @Test
    public void testCreateTerritoryViaAPI() {
        final TerritoryBuilder territoryBuilder = game.getNewTerritoryBuilder();
        territoryBuilder.initializeTerritory(new Size(TERRITORY_SIZE, TERRITORY_SIZE));
        territoryBuilder.defaultHamsterAt(Location.ORIGIN, Direction.EAST, 0);
        territoryBuilder.grainAt(Location.from(1, 0));

        game.initialize(territoryBuilder);

        paule = game.getTerritory().getDefaultHamster();
        assertEquals(game.getTerritory().getTerritorySize(), new Size(TERRITORY_SIZE, TERRITORY_SIZE));
        assertEquals(paule.getLocation(), Location.from(0, 0));
        assertEquals(game.getTerritory().getTotalGrainCount(), 1);
    }

}
