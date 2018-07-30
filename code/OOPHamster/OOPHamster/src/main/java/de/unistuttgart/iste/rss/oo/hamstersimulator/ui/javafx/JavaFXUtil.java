package de.unistuttgart.iste.rss.oo.hamstersimulator.ui.javafx;

import java.util.concurrent.CountDownLatch;

import javafx.application.Platform;

public final class JavaFXUtil {

    public static final void blockingExecuteOnFXThread(final Runnable runnable) {
        if (!Platform.isFxApplicationThread()) {
            final CountDownLatch doneLatch = new CountDownLatch(1);
            Platform.runLater(() -> {
                try {
                    runnable.run();
                } finally {
                    doneLatch.countDown();
                }
            });

            try {
                doneLatch.await();
            } catch (final InterruptedException ex) { }
        } else {
            runnable.run();
        }
    }

}
