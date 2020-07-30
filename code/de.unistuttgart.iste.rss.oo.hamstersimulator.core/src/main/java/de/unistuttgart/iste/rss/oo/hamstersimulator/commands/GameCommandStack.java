package de.unistuttgart.iste.rss.oo.hamstersimulator.commands;

import static de.unistuttgart.iste.rss.utils.Preconditions.checkState;

import java.util.concurrent.Semaphore;

import de.unistuttgart.iste.rss.oo.hamstersimulator.exceptions.GameAbortedException;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleDoubleProperty;

public class GameCommandStack extends CommandStack {

    public enum Mode {
        RUNNING, INITIALIZING, STOPPED, PAUSED
    }

    private final ReadOnlyObjectWrapper<Mode> mode = new ReadOnlyObjectWrapper<Mode>(this, "mode", Mode.INITIALIZING);
    protected final SimpleDoubleProperty speed = new SimpleDoubleProperty(this, "speed", 4.0);

    private final Semaphore pauseLock = new Semaphore(1, true);

    private Thread executingThread;

    public void startGame(final boolean startPaused) {
        this.executedCommands.clear();
        this.canUndo.set(false);
        this.canRedo.set(false);
        if (startPaused) {
            pause();
        } else {
            mode.set(Mode.RUNNING);
        }
    }

    public void delay() {
        try {
            Thread.sleep((int)((11.0d-this.speed.get())/5.0d * 400.0d));
        } catch (final InterruptedException e) {
        }
    }

    public void setSpeed(final double speed) {
        assert speed > 0 && speed <= 10;
        this.speed.set(speed);
    }

    @Override
    public void execute(final Command command) {
        if (this.mode.get() != Mode.RUNNING && this.mode.get() != Mode.PAUSED) {
            throw new GameAbortedException("The game needs to be running to execute hamster commands");
        }
        if (!command.canExecute()) {
            mode.set(Mode.STOPPED);
            throw command.getExceptionsFromPreconditions().get(0);
        }
        this.executingThread = Thread.currentThread();
        checkState(!(mode.get() == Mode.STOPPED));
        try {
            pauseLock.acquire();
            super.execute(command);
            delay();
        } catch (final InterruptedException e) {
        } catch (final Exception e) {
            // Stop the game to prevent execution of further commands!
            mode.set(Mode.STOPPED);
            throw e;
        }
        finally {
            pauseLock.release();
        }
    }

    /*@
     @ requires true;
     @ !ensures this.canUndo.get();
     @ !ensures this.canRedo.get();
     @ ensures this.executedCommands.isEmpty();
     @ ensures this.undoneCommands.isEmpty();
     @ ensures this.mode.get() == Mode.INITIALIZING
     @*/
    /**
     * hard-resets the CommandStack. it clears executedCommands and undoneCommands, however, it does NOT
     * undo all commands. If this behaviour is desired, it is necessary to call undoAll first.
     * This also sets the mode to initializing.
     */
    @Override
    public void hardReset() {
        super.hardReset();

        this.stopGame();
        this.mode.set(Mode.INITIALIZING);
    }

    private void interruptWaitingThreads() {
        if (this.pauseLock.hasQueuedThreads()) {
            this.executingThread.interrupt();
        }
    }

    public void stopGame() {
        mode.set(Mode.STOPPED);
        if (pauseLock.availablePermits() == 0) {
            pauseLock.release();
        }
        interruptWaitingThreads();
    }

    public void pauseAsync() {
        assert mode.get() == Mode.RUNNING;

        mode.set(Mode.PAUSED);
        new Thread(() -> {
            try {
                this.pauseLock.acquire();
            } catch (final InterruptedException e) {
            }
        }).start();
    }

    public void pause() {
        assert mode.get() == Mode.RUNNING;

        mode.set(Mode.PAUSED);
        try {
            this.pauseLock.acquire();
        } catch (final InterruptedException e) {
        }
    }

    public void resume() {
        assert this.pauseLock.availablePermits() == 0;
        assert this.mode.get() == Mode.PAUSED;

        mode.set(Mode.RUNNING);
        this.pauseLock.release();
    }

    public ReadOnlyObjectProperty<Mode> modeProperty() {
        return this.mode.getReadOnlyProperty();
    }

    public DoubleProperty speedProperty() {
        return this.speed;
    }

}
