package de.unistuttgart.iste.rss.oo.hamstersimulator.testFramework;

import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.HamsterGameViewModel;
import de.unistuttgart.iste.rss.oo.hamstersimulator.external.model.SimpleHamsterGame;

public class TestUtils {

    private final HamsterGameViewModel viewModel;
    private final SimpleHamsterGame game;

    public TestUtils(final SimpleHamsterGame game) {
        this.viewModel = game.getGameViewModel();
        this.game = game;
        this.viewModel.getGameController().disableDelay();
    }

    public final HamsterGameViewModel getViewModel() {
        return this.viewModel;
    }

    public void runGame() {
        this.game.doRun();
    }
}
