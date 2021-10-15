package de.hamstersimulator.objectsfirst.main.tests;

import de.hamstersimulator.objectsfirst.adapter.HamsterGameViewModel;
import de.hamstersimulator.objectsfirst.adapter.InputInterface;
import de.hamstersimulator.objectsfirst.datatypes.Direction;
import de.hamstersimulator.objectsfirst.datatypes.Location;
import de.hamstersimulator.objectsfirst.datatypes.Size;
import de.hamstersimulator.objectsfirst.external.model.HamsterGame;
import de.hamstersimulator.objectsfirst.external.model.TerritoryBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;


/**
 * Class which provides an easy way to run hamster games for test cases
 * Provides a new game for each test case
 */
public class BaseHamsterGameTest {

    /**
     * HamsterGame used to execute tests, set with BeforeEach
     */
    protected HamsterGame game;

    /**
     * Initializes game as 10x10 territory with the default hamster in the top left corner facing south and 10 grains
     * Also adds a mock InputInterface and starts the game
     */
    @BeforeEach
    public void initGame() {
        game = new HamsterGame();
        final TerritoryBuilder builder = game.getNewTerritoryBuilder();
        builder.initializeTerritory(new Size(10, 10));
        builder.defaultHamsterAt(Location.from(0, 0), Direction.SOUTH, 10);
        game.initialize(builder);
        final HamsterGameViewModel viewModel = game.getModelViewAdapter();
        viewModel.addInputInterface(getInputInterfaceMock());
        viewModel.getGameController().disableDelay();
        game.startGame();
    }

    /**
     * Gets a mock for the input interface
     * @return the mock
     */
    private InputInterface getInputInterfaceMock() {
        return Mockito.mock(InputInterface.class);
    }

    /**
     * Gets the viewModel of the game
     * @return the viewModel, != null
     */
    protected HamsterGameViewModel getViewModel() {
        return game.getModelViewAdapter();
    }
}
