package de.unistuttgart.iste.rss.oo.hamstersimulator.commands;

import static de.unistuttgart.iste.rss.oo.hamstersimulator.util.Preconditions.checkArgument;
import static de.unistuttgart.iste.rss.oo.hamstersimulator.util.Preconditions.checkState;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.Semaphore;

import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.InitHamsterCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.MoveCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.PickGrainCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.PutGrainCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.TurnLeftCommand;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleDoubleProperty;

public class GameCommandStack<T extends Command> extends CommandStack<T> {

    public enum Mode {
        RUNNING, INITIALIZING, STOPPED, PAUSED
    }

    private final ReadOnlyObjectWrapper<Mode> state = new ReadOnlyObjectWrapper<Mode>(this, "state", Mode.INITIALIZING);
    protected final SimpleDoubleProperty speed = new SimpleDoubleProperty(this, "speed", 5.0);

    private final Semaphore pauseLock = new Semaphore(1, true);

    private final Collection<Class<?>> allowedGameCommands = Arrays.asList(MoveCommand.class, PickGrainCommand.class,
            PutGrainCommand.class, TurnLeftCommand.class, InitHamsterCommand.class);
    private Thread executingThread;

    public void startGame() {
        this.executedCommands.clear();
        this.canUndo.set(false);
        this.canRedo.set(false);
        state.set(Mode.PAUSED);
        try {
            this.pauseLock.acquire();
        } catch (final InterruptedException e) {
        }
    }

    public void delay() {
        try {
            Thread.sleep((int)(this.speed.get() * 200.0d));
        } catch (final InterruptedException e) {
        }
    }

    public void setSpeed(final double speed) {
        assert speed > 0 && speed <= 10;
        this.speed.set(speed);
    }

    @Override
    public void execute(final T command) {
        try {
            this.executingThread = Thread.currentThread();
            checkState(!(state.get() == Mode.STOPPED));
            checkArgument(state.get() == Mode.INITIALIZING || allowedGameCommands.contains(command.getClass()),
                    "Only game commands may be executed in game mode!");
            pauseLock.acquire();
            super.execute(command);
            pauseLock.release();
            delay();
        } catch (final InterruptedException e) {
        }
    }

    public void reset() {
        state.set(Mode.INITIALIZING);
        interruptWaitingThreads();
    }

    private void interruptWaitingThreads() {
        if (this.pauseLock.hasQueuedThreads()) {
            this.executingThread.interrupt();
        }
    }

    public void stopGame() {
        state.set(Mode.STOPPED);
        if (pauseLock.availablePermits() == 0) {
            pauseLock.release();
        }
        interruptWaitingThreads();
    }

    public void pause() {
        assert state.get() != Mode.PAUSED;

        state.set(Mode.PAUSED);
        new Thread(() -> {
            try {
                this.pauseLock.acquire();
            } catch (final InterruptedException e) {
            }
        }).start();
    }

    public void resume() {
        assert this.pauseLock.availablePermits() == 0;

        state.set(Mode.RUNNING);
        this.pauseLock.release();
    }

    public ReadOnlyObjectProperty<Mode> stateProperty() {
        return this.state.getReadOnlyProperty();
    }

    public DoubleProperty speedProperty() {
        return this.speed;
    }

}
