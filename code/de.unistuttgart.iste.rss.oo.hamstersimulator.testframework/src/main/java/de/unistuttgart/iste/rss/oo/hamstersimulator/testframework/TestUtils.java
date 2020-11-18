package de.unistuttgart.iste.rss.oo.hamstersimulator.testframework;

import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.HamsterGameViewModel;
import de.unistuttgart.iste.rss.oo.hamstersimulator.external.model.SimpleHamsterGame;

/**
 * Utils to test a SimpleHamsterGame
 * Provides the viewmodel of the underlying game and makes it possible to execute the game
 */
public class TestUtils {

    /**
     * HamsterGameViewModel of the underlying game
     */
    private final HamsterGameViewModel viewModel;
    /**
     * the SimpleHamsterGame used to execute the game
     */
    private final SimpleHamsterGame game;

    /**
     * Creates a new TestUtils instance which the specified SimpleHamsterGame
     * @param game the SimpleHamsterGame used to execute the HamsterGame
     */
    public TestUtils(final SimpleHamsterGame game) {
        this.viewModel = game.getGameViewModel();
        this.game = game;
        this.viewModel.getGameController().disableDelay();
    }

    /**
     * Gets the viewmodel of the underlying HamsterGame
     * @return the viewmodel which represents the HamsterGame
     */
    public final HamsterGameViewModel getViewModel() {
        return this.viewModel;
    }

    /**
     * Runs the underlying HamsterGame by calling doRun on the
     * SimpleHamsterGame
     */
    public void runGame() {
        this.game.doRun();
    }
}
