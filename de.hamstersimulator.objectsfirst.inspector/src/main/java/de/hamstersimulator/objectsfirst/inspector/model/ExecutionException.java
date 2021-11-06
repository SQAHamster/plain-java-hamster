package de.hamstersimulator.objectsfirst.inspector.model;

import java.lang.reflect.InvocationTargetException;

/**
 * Wrapper class for non-runtime Exceptions, with a static helper method to
 * return causing RuntimeExceptions or return new ExecutionExceptions for InvocationTargetExceptions
 */
public final class ExecutionException extends RuntimeException {

    /**
     * Creates a new ExecutionException with a message
     *
     * @param cause the Throwable with caused the Exception
     */
    private ExecutionException(Throwable cause) {
        super("Method execution caused non-runtime Exception: " + cause.getMessage(), cause);
    }

    /**
     * Returns an exception representing the given InvocationTargetException
     * If the cause is a RuntimeException, it is returned, otherwise a new
     * InvocationTargetException is created and returned
     *
     * @param exception an InvocationTargetException which caused a reflected method
     *                  call to fail
     */
    public static RuntimeException getForException(InvocationTargetException exception) {
        final Throwable cause = exception.getCause();
        if (cause instanceof RuntimeException) {
            return (RuntimeException)cause;
        } else {
            return new ExecutionException(cause);
        }
    }
}
