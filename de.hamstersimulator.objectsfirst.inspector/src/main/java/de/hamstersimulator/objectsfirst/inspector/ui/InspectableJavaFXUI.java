package de.hamstersimulator.objectsfirst.inspector.ui;

import de.hamstersimulator.objectsfirst.adapter.HamsterGameViewModel;
import de.hamstersimulator.objectsfirst.inspector.viewmodel.InspectionViewModel;
import de.hamstersimulator.objectsfirst.ui.javafx.JavaFXUI;

import java.io.IOException;

public class InspectableJavaFXUI {

    /*@
     @ requires true;
     @*/
    /**
     * Displays the hamster game and the class/instance inspector associated with the provided hamster game adapter
     * in a new window.
     * This automatically starts the UI, adds the necessary input interface and opens the scene.
     *
     * @param hamsterGameViewModel the adapter of the hamster game to display, must be != null
     * @param inspect              The view model of the class/instance inspector to be bound to by the GUI, must be != null
     */
    public static void displayInNewGameWindow(final HamsterGameViewModel hamsterGameViewModel, final InspectionViewModel inspect) {
        JavaFXUI.openSceneFor(inputInterface -> {
            hamsterGameViewModel.addInputInterface(inputInterface);
            try {
                return new InpectableHamsterGameStage(hamsterGameViewModel, inspect);
            } catch (final IOException e) {
                e.printStackTrace();
                return null;
            }
        });
    }
}
