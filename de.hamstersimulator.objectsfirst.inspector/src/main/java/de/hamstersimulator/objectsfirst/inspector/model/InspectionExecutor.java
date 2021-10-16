package de.hamstersimulator.objectsfirst.inspector.model;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public final class InspectionExecutor {

    //@ public instance invariant isExecuting ==> (executingThread != null);

    private final BlockingQueue<Runnable> runnableQueue = new LinkedBlockingQueue<>();

    private volatile Thread executingThread;
    private volatile boolean isExecuting;

    /**
     * Executes incoming runnables until a RuntimeException is thrown
     */
    public void blockingExecute() {
        this.executingThread = Thread.currentThread();
        this.isExecuting = true;
        try {
            while (this.isExecuting) {
                final Runnable nextRunnable = this.runnableQueue.take();
                nextRunnable.run();
            }
        } catch (InterruptedException e) { }
    }

    /**
     * Schedules a runnable to execute
     * @param runnable the runnable to execute
     */
    public void scheduleRunnable(final Runnable runnable) {
        this.runnableQueue.add(runnable);
    }

    /**
     * Stops the execution
     * Interrupts the waiting execution thread
     */
    public void stop() {
        if (this.isExecuting) {
            this.isExecuting = false;
            this.executingThread.interrupt();
        }
    }
}
