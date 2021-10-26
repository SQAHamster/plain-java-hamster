package de.hamstersimulator.objectsfirst.inspector;

import de.hamstersimulator.objectsfirst.datatypes.Location;
import de.hamstersimulator.objectsfirst.external.model.Hamster;
import de.hamstersimulator.objectsfirst.external.simple.game.SimpleHamsterGame;
import de.hamstersimulator.objectsfirst.inspector.model.InspectionExecutor;
import de.hamstersimulator.objectsfirst.inspector.ui.InspectableJavaFXUI;
import de.hamstersimulator.objectsfirst.inspector.viewmodel.ClassInstanceManager;
import de.hamstersimulator.objectsfirst.inspector.viewmodel.InspectionViewModel;
import de.hamstersimulator.objectsfirst.ui.javafx.JavaFXUtil;

/**
 * Parent class of a simple, to a large extend preconfigured hamster game.
 * To be used in lectures 2-8 of PSE as predefined base class.
 *
 * @author Steffen Becker
 */
public abstract class InspectableSimpleHamsterGame extends SimpleHamsterGame {

    private final InspectionViewModel inspect;

    public InspectableSimpleHamsterGame() {
        super();
        this.inspect = new InspectionViewModel();
        this.initializeInspection(this.inspect);
    }

    protected void initializeInspection(final InspectionViewModel inspect) {
        final ClassInstanceManager manager = this.inspect.getClassInstanceManager();
        manager.addInstance(this, "simpleHamsterGame", true);
        manager.addInstance(this.paule, "paule", false);
        manager.addInstance(this.game, "game", false);
        manager.addInstance(this.game.getTerritory(), "territory", false);
        manager.addClass(Hamster.class, false);
        manager.addClass(Location.class, false);
        manager.addClassesFromClassPackage(this.getClass());
    }

    /**
     * Displays the hamster game in a new game window
     * The UI type can be specified in the config file or in the environment variable
     * OUTPUT_INTERFACE. Possible values are "JAVA_FX" (inspection GUI), "HTTP" and "NONE"
     * The default is "JAVA_FX" (inspection GUI).
     */
    @Override
    protected void displayInNewGameWindow() {
        final String mode = this.getRequestedUIMode();
        if (UIMode.JAVA_FX.equals(mode)) {
            InspectableJavaFXUI.displayInNewGameWindow(this.game.getModelViewAdapter(), this.inspect);
        } else {
            super.displayInNewGameWindow();
        }
    }

    /**
     * Executed after the run method was executed
     * Can be used, to perform cleanup, or start UI tools
     * Warning: might not be executed!
     */
    @Override
    protected void postRun() {
        final InspectionExecutor executor = new InspectionExecutor();
        JavaFXUtil.blockingExecuteOnFXThreadIfAvailable(new Runnable() {
            @Override
            public void run() {
                InspectableSimpleHamsterGame.this.inspect.setExecutor(executor);
            }
        });
        try {
            executor.blockingExecute();
        } finally {
            JavaFXUtil.blockingExecuteOnFXThreadIfAvailable(this.inspect::removeExecutor);
        }
    }
}
