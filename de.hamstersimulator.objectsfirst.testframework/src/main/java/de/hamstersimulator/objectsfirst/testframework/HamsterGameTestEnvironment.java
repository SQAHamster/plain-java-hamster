package de.hamstersimulator.objectsfirst.testframework;

import de.hamstersimulator.objectsfirst.adapter.HamsterGameViewModel;
import de.hamstersimulator.objectsfirst.external.model.SimpleHamsterGame;

/**
 * Environment to test a SimpleHamsterGame
 * Provides the viewmodel of the underlying game.
 * If runGame is called, doRun is called on the provided game. It is possible to call runGame
 * multiple times, however this might result is strange behaviour and therefore is discouraged
 * For an example, see TestUtilsHamsterGameTest in tests
 */
public class HamsterGameTestEnvironment {

    /**
     * HamsterGameViewModel of the underlying game.
     */
    private final HamsterGameViewModel viewModel;

    /**
     * the SimpleHamsterGame used to execute the game.
     */
    private final SimpleHamsterGame game;

    /**
     * Creates a new HamsterGameTestEnvironment instance which the specified SimpleHamsterGame.
     * Also disables the delay on the provided game
     * To get the desired behaviour, it is encouraged to provide a SimpleHamsterGame which
     * was not executed before
     * @param targetGame the SimpleHamsterGame used to execute the HamsterGame
     */
    public HamsterGameTestEnvironment(final SimpleHamsterGame targetGame) {
        this.viewModel = targetGame.getGameViewModel();
        this.game = targetGame;
        this.viewModel.getGameController().disableDelay();
    }

    /**
     * Gets the viewmodel of the underlying HamsterGame.
     * @return the viewmodel which represents the HamsterGame
     */
    public final HamsterGameViewModel getViewModel() {
        return this.viewModel;
    }

    /**
     * Runs the underlying HamsterGame by calling doRun on the
     * SimpleHamsterGame
     * Warning: it is possible to call this multiple times, and each time doRun is called on the
     * SimpleHamsterGame. This might cause strange behavior and therefore is highly discouraged
     */
    public void runGame() {
        this.game.doRun();
    }
}
