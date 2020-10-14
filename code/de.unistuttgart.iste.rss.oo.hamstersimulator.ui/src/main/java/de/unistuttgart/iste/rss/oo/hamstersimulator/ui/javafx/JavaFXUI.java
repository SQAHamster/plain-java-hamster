package de.unistuttgart.iste.rss.oo.hamstersimulator.ui.javafx;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.HamsterGameViewModel;
import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.InputInterface;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

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
     * @param hamsterGameViewModel the adapter of the hamster game to display, must be != null
     */
    public static void displayInNewGameWindow(final HamsterGameViewModel hamsterGameViewModel) {
        hamsterGameViewModel.addInputInterface(inputInterface);
        openSceneFor(hamsterGameViewModel);
    }

    /*@
     @ requires true;
     @ ensures isStarted;
     @*/
    /**
     * Starts the javafx ui thread if not already started
     */
    public static void start() {
        if (!isStarted) {
            new Thread(()->Application.launch(JavaFXUI.class)).start();
            waitForJavaFXStart();
            isStarted = true;
            Platform.setImplicitExit(true);
        }
    }
    
    public static void setKeepFXRunningAfterLastWindow() {
        Platform.setImplicitExit(false);        
    }
    
    @Override
    public void stop() throws Exception {
        isStarted = false;
        super.stop();
    }

    private static void waitForJavaFXStart() {
        try {
            initLatch.await();
        } catch (final InterruptedException e) { }
    }

    @Override
    public void start(final Stage primaryStage) throws Exception {
        initLatch.countDown();
    }

    /*@
     @ requires true;
     @ ensures isStarted;
     @*/
    /**
     * Opens a scene for the hamster game associated with hamsterGameViewModel
     * requires that the JavaFXUI is started
     * @param hamsterGameViewModel the adapter for the hamster game to display
     */
    public static void openSceneFor(final HamsterGameViewModel hamsterGameViewModel) {
        start();
        JavaFXUtil.blockingExecuteOnFXThread(() -> {
            Stage stage;
            try {
                stage = new HamsterGameStage(hamsterGameViewModel);
                stage.show();
            } catch (final IOException e) {
                e.printStackTrace();
            }
        });
    }

    /*@
     @ requires true;
     @ ensures \result != null;
     @*/
    /**
     * Getter for the JavaFXInputInterface singleton
     * @return the JavaFXInputInterface to display
     */
    public static InputInterface getJavaFXInputInterface() {
        return inputInterface;
    }

}
