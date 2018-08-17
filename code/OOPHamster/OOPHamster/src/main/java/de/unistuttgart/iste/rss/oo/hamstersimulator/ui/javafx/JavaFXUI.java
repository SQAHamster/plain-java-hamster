package de.unistuttgart.iste.rss.oo.hamstersimulator.ui.javafx;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import de.unistuttgart.iste.rss.oo.hamstersimulator.external.model.HamsterGame;
import javafx.application.Application;
import javafx.stage.Stage;

public class JavaFXUI extends Application {

    private static final CountDownLatch initLatch = new CountDownLatch(1);

    public static void start() {
        new Thread(()->Application.launch(JavaFXUI.class)).start();
        waitForJavaFXStart();
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

    public static void openSceneFor(final HamsterGame hamsterGame) {
        JavaFXUtil.blockingExecuteOnFXThread(() -> {
            Stage stage;
            try {
                stage = new HamsterGameStage(hamsterGame);
                stage.show();
            } catch (final IOException e) {
                e.printStackTrace();
            }
        });
    }

}
