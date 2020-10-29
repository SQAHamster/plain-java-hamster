package de.unistuttgart.iste.rss.oo.hamstersimulator.server.internal;



import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.InputInterface;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.locks.ReentrantLock;

import static de.unistuttgart.iste.rss.utils.Preconditions.*;

/**
 * The input interface that handles remote requests.
 * This class is able to deal with concurrent results. <br>
 * This class is inherently multithreaded: if single threaded
 * execution is necessary (e.g. a setResult after a getInputId), use
 * enterCriticalRegion and leaveCriticalRegion. <br>
 */
public abstract class RemoteInputInterface implements InputInterface {
    private volatile CompletableFuture<Optional<String>> result = new CompletableFuture<>();

    /**
     * A simple counter used to distinguish between input requests
     * so no outdated requests terminate newer ones
     */
    private volatile int inputIdCounter = 0;
    /**
     * The current input mode
     */
    private volatile InputMode mode = InputMode.NONE;
    /**
     * The current message message a remote UI should display
     */
    private volatile InputMessage message;

    /**
     * lock for critical sections
     */
    private final ReentrantLock lock = new ReentrantLock();

    /**
     * Read an integer value from a user. This blocks until there is
     * an integer to return or it is aborted.
     * This is thread safe
     * @param message The message used in the prompt for the number.
     * @return The integer value read or an empty optional, if aborted.
     */
    @Override
    public Optional<Integer> readInteger(String message) {
        this.enterCriticalRegion();
        try {
            this.setupNext();
            this.mode = InputMode.READ_INT;
            this.message = new InputMessage(message, getInputID());
            onInput(this.message);
        } finally {
            this.leaveCriticalRegion();
        }

        try {
            final Optional<String> res = result.get();
            if (res.isEmpty()) {
                return Optional.empty();
            } else {
                final int i = Integer.parseInt(res.get());
                return Optional.of(i);
            }
        } catch (InterruptedException | ExecutionException e) {
            return Optional.of(0);
        }
    }

    /**
     * Read a string value from a user. This blocks until there is a
     * String to return or it is aborted
     * This is thread safe
     * @param message The message used in the prompt for the string.
     * @return The string value read or an empty optional, if aborted.
     */
    @Override
    public Optional<String> readString(String message) {
        this.enterCriticalRegion();
        try {
            this.setupNext();
            this.mode = InputMode.READ_STRING;
            this.message = new InputMessage(message, getInputID());
            onInput(this.message);
        } finally {
            this.leaveCriticalRegion();
        }

        try {
            return this.result.get();
        } catch (InterruptedException | ExecutionException e) {
            return Optional.empty();
        }
    }

    /**
     * Informs a user about an abnormal execution aborting.
     * This blocks until it returns or is aborted
     * This is thread safe
     * @param throwable The throwable which lead to aborting the program.
     */
    @Override
    public void confirmAlert(Throwable throwable) {
        this.enterCriticalRegion();
        try {
            this.setupNext();
            this.mode = InputMode.SHOW_ALERT;
            final StringWriter stringWriter = new StringWriter();
            final PrintWriter printWriter = new PrintWriter(stringWriter);
            throwable.printStackTrace(printWriter);

            this.message = new InputMessage(throwable.getMessage(), getInputID(),
                    throwable.getClass().getSimpleName(), stringWriter.toString());
            onInput(this.message);
        } finally {
            this.leaveCriticalRegion();
        }

        try {
            this.result.get();
        } catch (InterruptedException | ExecutionException e) {
            // ignore
        }
    }

    /**
     * Aborts readInteger, readString or showAlert.
     * This is thread-safe.
     * May be called multiple times, even if no input is pending
     */
    @Override
    public void abort() {
        this.mode = InputMode.NONE;
        this.result.complete(null);
    }

    /**
     * Setups the next input request by increasing the id counter and adding a new
     * result future.
     * This requires that there is currently no input request
     * @throws IllegalStateException if there is still a pending input requests
     */
    @SuppressWarnings("NonAtomicOperationOnVolatileField")
    private void setupNext() {
        checkState(this.mode == InputMode.NONE, "parallel requests are not supported");

        enterCriticalRegion();
        try {
            inputIdCounter++;
            this.result = new CompletableFuture<>();
        } finally {
            leaveCriticalRegion();
        }

    }

    /**
     * Sets the result from a remote source
     * @param result the result of the request
     *               normally, this should not be null, however it can
     * @param id the id of the request (only has an effect
     * @throws IllegalArgumentException if the result comes from an outdated request
     */
    public void setResult(final String result, final int id) {
        checkArgument(id == this.inputIdCounter, "illegal input id, possibly outdated");

        enterCriticalRegion();
        try {
            this.mode = InputMode.NONE;
            this.result.complete(Optional.ofNullable(result));
        } finally {
            leaveCriticalRegion();
        }
    }

    /**
     * Gets the id of the current input request
     * This id must be provided in setResult to ensure
     * that a previous request does not set the current result
     * @return the id
     */
    public int getInputID() {
        return this.inputIdCounter;
    }

    /**
     * Gets the current input mode
     * @return the current input mode
     */
    public InputMode getInputMode() {
        return this.mode;
    }

    /**
     * Gets the message the UI should display <br>
     * If the input mode is READ_STRING or READ_INT, it is just a simple
     * string. If the input mode is SHOW_ALERT, it is a json with the
     * fields type, message and stacktrace.
     * @return the current message or an empty optional if
     *         there is none because the input mode is none
     */
    public Optional<InputMessage> getMessage() {
        if (this.mode == InputMode.NONE) {
            return Optional.empty();
        } else {
            return Optional.of(this.message);
        }
    }

    protected abstract void onInput(final InputMessage inputMessage);

    /**
     * Enters a critical region and waits if necessary <br>
     * In a critical region, the following things are guaranteed:
     * <ul>
     *     <li>the input mode does not change</li>
     *     <li>the message does not change</li>
     *     <li>the input id does not change</li>
     * </ul>
     * Use with care! Do not spend too much time in a critical region. Make sure
     * to leave the critical region after the necessary part is done.
     */
    public void enterCriticalRegion() {
        this.lock.lock();
    }

    /**
     * leaves a critical region
     * @throws IllegalMonitorStateException if the executing thread did not enter a critical region
     */
    public void leaveCriticalRegion() {
        this.lock.unlock();
    }
}
