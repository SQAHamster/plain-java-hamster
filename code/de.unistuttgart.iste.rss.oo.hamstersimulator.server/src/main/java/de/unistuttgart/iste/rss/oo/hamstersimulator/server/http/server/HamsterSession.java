package de.unistuttgart.iste.rss.oo.hamstersimulator.server.http.server;

import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Mode;
import de.unistuttgart.iste.rss.oo.hamstersimulator.server.communication.Operation;
import de.unistuttgart.iste.rss.oo.hamstersimulator.server.communication.clienttoserver.SpeedChangedOperation;
import de.unistuttgart.iste.rss.oo.hamstersimulator.server.communication.clienttoserver.*;
import de.unistuttgart.iste.rss.oo.hamstersimulator.server.communication.servertoclient.*;
import de.unistuttgart.iste.rss.oo.hamstersimulator.server.delta.Delta;
import de.unistuttgart.iste.rss.oo.hamstersimulator.server.internal.GameState;
import de.unistuttgart.iste.rss.oo.hamstersimulator.server.internal.GameStatus;
import de.unistuttgart.iste.rss.oo.hamstersimulator.server.internal.InputMessage;
import de.unistuttgart.iste.rss.utils.LambdaVisitor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;

public class HamsterSession {

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final List<Delta> deltaList = new ArrayList<>();
    private final ObjectOutputStream outputStream;
    private final LambdaVisitor<Operation, Runnable> operationVisitor;
    private final int id;
    private final Socket socket;

    private volatile Mode mode;
    private volatile InputMessage inputMessage;
    private volatile boolean canUndo;
    private volatile boolean canRedo;
    private volatile double speed;

    public HamsterSession(final Socket socket, final int id) throws IOException {
        this.id = id;
        this.socket = socket;
        this.outputStream = new ObjectOutputStream(socket.getOutputStream());
        this.operationVisitor = new LambdaVisitor<Operation, Runnable>()
                .on(AbortInputOperation.class).then(operation -> () -> onAbortInput(operation))
                .on(AddDeltaOperation.class).then(operation -> () -> onAddDelta(operation))
                .on(AddDeltasOperation.class).then(operation -> () -> onAddDeltas(operation))
                .on(RequestInputOperation.class).then(operation -> () -> onRequestInput(operation))
                .on(ModeChangedOperation.class).then(operation -> () -> onModeChanged(operation))
                .on(CanUndoChangedOperation.class).then(operation -> () -> onCanUndoChanged(operation))
                .on(CanRedoChangedOperation.class).then(operation -> () -> onCanRedoChanged(operation))
                .on(SpeedChangedOperation.class).then(operation -> () -> onSpeedChanged(operation));

        startInputListener(socket);
    }

    private void startInputListener(final Socket socket) throws IOException {
        final ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
        new Thread(() -> {
            try {
                while (!socket.isClosed()) {
                    final Object input = inputStream.readObject();
                    final Runnable handler = operationVisitor.apply(input);
                    if (handler == null) {
                        throw new IllegalStateException("no handler found for the operation");
                    } else {
                        handler.run();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                // ok, connection closed
                // TODO shutdown
            }
        }).start();
    }

    public List<Delta> getDeltasSince(final int index) {
        readWriteLock.readLock().lock();
        try {
            return new ArrayList<>(this.deltaList.subList(Math.min(Math.max(index, 0),
                    this.deltaList.size()), this.deltaList.size()));
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    public GameState getGameState() {
        readWriteLock.readLock().lock();
        try {
            return new GameState(mode, inputMessage, canUndo, canRedo, speed);
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    public synchronized void sendOperation(final Operation operation) {
        try {
            outputStream.writeObject(operation);
        } catch (IOException e) {
            //TODO shutdown
            e.printStackTrace();
        }
    }

    private void onAbortInput(final AbortInputOperation operation) {
        readWriteLock.writeLock().lock();
        try {
            this.inputMessage = null;
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    private void onAddDelta(final AddDeltaOperation operation) {
        readWriteLock.writeLock().lock();
        try {
            deltaList.add(operation.getDelta());
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    private void onAddDeltas(final AddDeltasOperation operation) {
        readWriteLock.writeLock().lock();
        try {
            deltaList.addAll(operation.getDeltaList());
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    private void onRequestInput(final RequestInputOperation operation) {
        readWriteLock.writeLock().lock();
        try {
            this.inputMessage = operation.getMessage();
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    private void onCanUndoChanged(final CanUndoChangedOperation operation) {
        readWriteLock.writeLock().lock();
        try {
            this.canUndo = operation.isCanUndo();
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    private void onCanRedoChanged(final CanRedoChangedOperation operation) {
        readWriteLock.writeLock().lock();
        try {
            this.canRedo = operation.isCanRedo();
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    private void onSpeedChanged(final SpeedChangedOperation operation) {
        readWriteLock.writeLock().lock();
        try {
            this.speed = operation.getSpeed();
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    private void onModeChanged(final ModeChangedOperation operation) {
        readWriteLock.writeLock().lock();
        try {
            this.mode = operation.getMode();
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    public void undo() {
        sendOperation(new UndoOperation());
    }

    public void redo() {
        sendOperation(new RedoOperation());
    }

    public void resume() {
        sendOperation(new ResumeOperation());
    }

    public void pause() {
        sendOperation(new PauseOperation());
    }

    public void setInputResult(final int inputId, final String result) {
        sendOperation(new SetInputOperation(inputId, result));
    }

    public void changeSpeed(final double speed) {
        sendOperation(new ChangeSpeedOperation(speed));
    }

    public GameStatus getStatus(final int since) {
        this.readWriteLock.readLock().lock();
        try {
            return new GameStatus(getGameState(), getDeltasSince(since));
        } finally {
            this.readWriteLock.readLock().unlock();
        }
    }

    public InputMessage getInputMessage() {
        this.readWriteLock.readLock().lock();
        try {
            return inputMessage;
        } finally {
            this.readWriteLock.readLock().unlock();
        }
    }
}
