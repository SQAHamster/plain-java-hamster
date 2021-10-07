package de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.ui;

import de.unistuttgart.iste.sqa.oo.hamstersimulator.adapter.HamsterGameViewModel;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.config.HamsterConfig;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.exceptions.GameAbortedException;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.external.model.Hamster;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.external.model.HamsterGame;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.external.model.SimpleHamsterGame;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.viewmodel.InspectionViewModel;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.server.http.client.HamsterClient;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.ui.javafx.JavaFXUI;

import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static de.unistuttgart.iste.sqa.utils.Preconditions.checkArgument;
import static de.unistuttgart.iste.sqa.utils.Preconditions.checkNotNull;

/**
 * Parent class of a simple, to a large extend preconfigured hamster game.
 * To be used in lectures 2-8 of PSE as predefined base class.
 *
 * @author Steffen Becker
 */
public class InspectableSimpleHamsterGame extends SimpleHamsterGame {

    InspectionViewModel inspect;

    public InspectableSimpleHamsterGame() {
        super();
        inspect = new InspectionViewModel(this.game);
    }

    @Override
    protected void displayInNewGameWindow() {
        InspectableJavaFXUI.displayInNewGameWindow(this.game.getModelViewAdapter(), inspect);
    }

    @Override
    protected void run() {
        game.startGame();
    }

    public static void main(String[] args) {
        InspectableSimpleHamsterGame game = new InspectableSimpleHamsterGame();
        game.loadTerritoryFromResourceFile("/example01.ter");
        game.displayInNewGameWindow();
        game.run();
    }
}
