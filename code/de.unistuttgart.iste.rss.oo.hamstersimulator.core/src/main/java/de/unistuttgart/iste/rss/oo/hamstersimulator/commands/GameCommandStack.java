package de.unistuttgart.iste.rss.oo.hamstersimulator.commands;

import static de.unistuttgart.iste.rss.utils.Preconditions.checkArgument;
import static de.unistuttgart.iste.rss.utils.Preconditions.checkState;

import java.nio.channels.FileChannel;
import java.util.concurrent.Semaphore;

import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.HamsterGameController;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Mode;
import de.unistuttgart.iste.rss.oo.hamstersimulator.exceptions.GameAbortedException;
import javafx.beans.InvalidationListener;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class GameCommandStack extends EditCommandStack implements HamsterGameController {

    private final ReadOnlyObjectWrapper<Mode> mode = new ReadOnlyObjectWrapper<Mode>(this, "mode", Mode.INITIALIZING);
    protected final SimpleDoubleProperty speed = new SimpleDoubleProperty(this, "speed", 4.0);

    private final Semaphore pauseLock = new Semaphore(1, true);

    private Thread executingThread;

    /**
     * Creates a new instance of the GameCommandStack
     */
    public GameCommandStack() {
        // prevent illegal values to be set via biding
        speed.addListener((observable, oldValue, newValue) -> {
            checkState(newValue.doubleValue() >= 0 && newValue.doubleValue() <= 10,
                    "Provided speed is not in range [0, 10]");
        });
    }

    /*@
     @ requires mode.get() == Mode.INITIALIZING;
     @ ensures startPaused ==> getCurrentGameMode() == Mode.PAUSED;
     @ ensures !startPaused ==> getCurrentGameMode() == Mode.RUNNING;
     @*/
    /**
     * Start the execution of a hamster game. Before executing start, no commands can be
     * executed by the hamster objects in the game.
     * This is only possible if the current mode is Mode.INITIALIZING
     * @param startPaused if true the game will be started in pause mode
     * @throws IllegalStateException if getCurrentGameMode() != Mode.INITIALIZING
     */
    public void startGame(final boolean startPaused) {
        checkState(mode.get() == Mode.INITIALIZING,
                "start game is only possible during initialization");

        this.executedCommands.clear();
        this.canUndo.set(false);
        this.canRedo.set(false);
        if (startPaused) {
            pause();
        } else {
            mode.set(Mode.RUNNING);
        }
    }

    private void delay() {
        try {
            Thread.sleep((int)((11.0d-this.speed.get())/5.0d * 400.0d));
        } catch (final InterruptedException e) {
        }
    }

    /*@
     @ requires speed >= 0 && speed <= MAX_SPEED;
     @ ensures getSpeed() == speed;
     @*/
    /**
     * Set the speed of the hamster game. Valid values are in the range from
     * 0.0 to 10.0,
     * where 0.0 is slow and 10.0 is fast.
     * @param speed The new game speed's delay. Has to be greater or equal 0.0 and
     *                  less than or equal 10.0.
     */
    @Override
    public void setSpeed(final double speed) {
        checkArgument(speed >= 0 && speed <= 10, "Provided speed is not in range [0, 10]");

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

    /*@
     @ requires true;
     @ ensures getCurrentGameMode() == Mode.STOPPED;
     */
    /**
     * Stop the execution of the game. The game is finished and needs to be reset / hardReset
     * or closed.
     * If the game is already stopped, this does nothing
     */
    @Override
    public void stopGame() {
        mode.set(Mode.STOPPED);
        if (pauseLock.availablePermits() == 0) {
            pauseLock.release();
        }
        interruptWaitingThreads();
    }

    /*@
     @ requires modeProperty().get() == Mode.RUNNING;
     @*/
    /**
     * Pauses the game when it is running.
     * If the game is not running (paused previously, not started or stopped), an exception
     * is thrown.
     * This is executed asynchronously, this methods returns before the game was paused.
     * Use with care! Normally, this is only necessary for UI functionality.
     * @throws IllegalStateException if modeProperty().get() != Mode.RUNNING
     */
    @Override
    public void pauseAsync() {
        checkState(mode.get() == Mode.RUNNING, "Cannot pause: game is not running");

        mode.set(Mode.PAUSED);
        new Thread(() -> {
            try {
                this.pauseLock.acquire();
            } catch (final InterruptedException e) {
            }
        }).start();
    }

    /*@
     @ requires modeProperty().get() == Mode.RUNNING;
     @ ensures modeProperty().get() == Mode.PAUSED;
     @*/
    /**
     * Pauses the game when it is running.
     * If the game is not running (paused previously, not started or stopped), an exception
     * is thrown.
     * This is executed synchronously, this methods returns after the game was successfully paused.
     * @throws IllegalStateException if modeProperty().get() != Mode.RUNNING
     */
    @Override
    public void pause() {
        checkState(mode.get() == Mode.RUNNING, "Cannot pause: game is not running");

        mode.set(Mode.PAUSED);
        try {
            this.pauseLock.acquire();
        } catch (final InterruptedException e) {
        }
    }

    /*@
     @ requires modeProperty().get() == Mode.PAUSED;
     @ ensures modeProperty().get() == Mode.RUNNING;
     @*/
    /**
     * Pauses the HamsterGame.
     * It is only possible to execute this in paused mode.
     *
     * @throws IllegalStateException if modeProperty().get() != Mode.PAUSED
     */
    @Override
    public void resume() {
        checkState(mode.get() == Mode.PAUSED, "Cannot resume: game is not paused");
        assert this.pauseLock.availablePermits() == 0;

        mode.set(Mode.RUNNING);
        this.pauseLock.release();
    }

    /**
     * Getter for the mode property
     * provides read-only access to the current mode of the associated game
     * @return the property (not null)
     */
    @Override
    public ReadOnlyObjectProperty<Mode> modeProperty() {
        return this.mode.getReadOnlyProperty();
    }

    /**
     * Getter for the speed property
     * provides read and write access to the current speed of the associated game
     * Note: if this property is set to an invalid value (not in range [0, 10]), an exception
     * is thrown
     *
     * @return the property (not null)
     */
    @Override
    public DoubleProperty speedProperty() {
        return this.speed;
    }

}
