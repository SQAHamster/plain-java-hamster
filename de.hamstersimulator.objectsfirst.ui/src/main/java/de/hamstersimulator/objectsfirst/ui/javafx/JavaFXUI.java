package de.hamstersimulator.objectsfirst.ui.javafx;

import de.hamstersimulator.objectsfirst.adapter.HamsterGameViewModel;
import de.hamstersimulator.objectsfirst.adapter.InputInterface;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.function.Function;

public class JavaFXUI extends Application {

    private static final CountDownLatch initLatch = new CountDownLatch(1);
    private static final JavaFXInputInterface inputInterface = new JavaFXInputInterface();
    private static volatile boolean isStarted = false;

    /*@
     @ requires true;
     @*/

    /**
     * Displays the hamster game associated with the provided hamster game adapter in a new window
     * This automatically starts the UI, adds the necessary input interface and opens the scene
     *
     * @param hamsterGameViewModel the adapter of the hamster game to display, must be != null
     */
    public static void displayInNewGameWindow(final HamsterGameViewModel hamsterGameViewModel) {
        JavaFXUI.openSceneFor(inputInterface -> {
            hamsterGameViewModel.addInputInterface(inputInterface);
            try {
                return new HamsterGameStage(hamsterGameViewModel);
            } catch (final IOException e) {
                e.printStackTrace();
                return null;
            }
        });
    }

    /*@
     @ requires true;
     @ ensures isStarted;
     @*/

    /**
     * Starts the javafx ui thread if not already started
     */
    public static void start() {
        if (!JavaFXUI.isStarted) {
            new Thread(() -> Application.launch(JavaFXUI.class)).start();
            JavaFXUI.waitForJavaFXStart();
            JavaFXUI.isStarted = true;
            Platform.setImplicitExit(true);
        }
    }

    public static void setKeepFXRunningAfterLastWindow() {
        Platform.setImplicitExit(false);
    }

    @Override
    public void stop() throws Exception {
        JavaFXUI.isStarted = false;
        super.stop();
    }

    private static void waitForJavaFXStart() {
        try {
            JavaFXUI.initLatch.await();
        } catch (final InterruptedException e) {
        }
    }

    @Override
    public void start(final Stage primaryStage) throws Exception {
        JavaFXUI.initLatch.countDown();
    }

    /*@
     @ requires true;
     @ ensures isStarted;
     @*/

    /**
     * Opens the GUI with the stage which is provided by the given stage supplier
     * requires that the JavaFXUI is started
     *
     * @param stageSupplier A function that provides ONE instance of a valid stage to display.
     *                      The function is provided the input interface for the gui to use
     *                      If `null` the stage won't be shown
     */
    public static void openSceneFor(final Function<InputInterface, Stage> stageSupplier) {
        JavaFXUI.start();
        JavaFXUtil.blockingExecuteOnFXThread(() -> {
            final Stage stage = stageSupplier.apply(JavaFXUI.inputInterface);
            if (stage != null) {
                stage.show();
            }
        });
    }

    /**
     * Returns whether the JavaFX UI has already been started or not.
     *
     * @return Returns `true` as soon as at least one JavaFX UI has been started (e.g. via `displayInNewGameWindow`)
     */
    public static boolean getIsStarted() {
        return JavaFXUI.isStarted;
    }

}
