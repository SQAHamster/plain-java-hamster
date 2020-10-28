package de.unistuttgart.iste.rss.oo.hamstersimulator.server.http.server;

import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Mode;
import de.unistuttgart.iste.rss.oo.hamstersimulator.server.communication.Operation;
import de.unistuttgart.iste.rss.oo.hamstersimulator.server.communication.clienttoserver.AbortInputOperation;
import de.unistuttgart.iste.rss.oo.hamstersimulator.server.communication.clienttoserver.AddDeltaOperation;
import de.unistuttgart.iste.rss.oo.hamstersimulator.server.communication.clienttoserver.AddDeltasOperation;
import de.unistuttgart.iste.rss.oo.hamstersimulator.server.communication.clienttoserver.RequestInputOperation;
import de.unistuttgart.iste.rss.oo.hamstersimulator.server.delta.Delta;
import de.unistuttgart.iste.rss.oo.hamstersimulator.server.internal.GameState;
import de.unistuttgart.iste.rss.oo.hamstersimulator.server.internal.InputMessage;
import de.unistuttgart.iste.rss.utils.LambdaVisitor;

import java.io.IOException;
import java.io.InputStream;
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
    private volatile Mode mode;
    private volatile InputMessage inputMessage;
    private final ObjectOutputStream outputStream;
    private final LambdaVisitor<Operation, Runnable> operationVisitor;

    public HamsterSession(Socket socket) throws IOException {
        this.outputStream = new ObjectOutputStream(socket.getOutputStream());
        this.operationVisitor = new LambdaVisitor<Operation, Runnable>()
                .on(AbortInputOperation.class).then(operation -> () -> onAbortInput(operation))
                .on(AddDeltaOperation.class).then(operation -> () -> onAddDelta(operation))
                .on(AddDeltasOperation.class).then(operation -> () -> onAddDeltas(operation))
                .on(RequestInputOperation.class).then(operation -> () -> onRequestInput(operation));

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

    private void addDeltas(final List<? extends Delta> deltas) {
        readWriteLock.writeLock().lock();
        try {
            deltaList.addAll(deltas);
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    public void withDeltasSince(final int index, final Consumer<? super Delta> consumer) {
        readWriteLock.readLock().lock();
        try {
            for (int i = Math.max(0, index); i < deltaList.size(); i++) {
                consumer.accept(this.deltaList.get(i));
            }
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    public GameState getGameState() {
        readWriteLock.readLock().lock();
        try {
            return new GameState(this.mode, this.inputMessage);
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    public synchronized void sendOperation(final Operation operation) throws IOException {
        outputStream.writeObject(operation);
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
            readWriteLock.writeLock().lock();
        }
    }

    private void onAddDeltas(final AddDeltasOperation operation) {
        readWriteLock.writeLock().lock();
        try {
            deltaList.addAll(operation.getDeltaList());
        } finally {
            readWriteLock.writeLock().lock();
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



}
