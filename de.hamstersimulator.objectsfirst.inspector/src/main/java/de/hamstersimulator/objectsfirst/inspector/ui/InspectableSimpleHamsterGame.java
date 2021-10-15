package de.hamstersimulator.objectsfirst.inspector.ui;

import de.hamstersimulator.objectsfirst.adapter.HamsterGameViewModel;
import de.hamstersimulator.objectsfirst.datatypes.Location;
import de.hamstersimulator.objectsfirst.exceptions.GameAbortedException;
import de.hamstersimulator.objectsfirst.external.model.Hamster;
import de.hamstersimulator.objectsfirst.external.model.HamsterGame;
import de.hamstersimulator.objectsfirst.external.model.SimpleHamsterGame;
import de.hamstersimulator.objectsfirst.inspector.viewmodel.InspectionViewModel;
import de.hamstersimulator.objectsfirst.ui.javafx.JavaFXUI;

import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static de.hamstersimulator.objectsfirst.utils.Preconditions.checkArgument;
import static de.hamstersimulator.objectsfirst.utils.Preconditions.checkNotNull;

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
        //TODO put in method
        inspect = new InspectionViewModel();
        inspect.createInstanceViewModel(this, "simpleHamsterGame");
        inspect.createInstanceViewModel(this.paule, "paule");
        inspect.viewModelForClass(Hamster.class);
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
