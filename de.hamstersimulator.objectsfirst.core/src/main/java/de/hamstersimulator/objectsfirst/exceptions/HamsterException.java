package de.hamstersimulator.objectsfirst.exceptions;

public abstract class HamsterException extends RuntimeException {

    /**
     * Serial
     */
    private static final long serialVersionUID = 6268135585948600913L;

    public HamsterException(final String message) {
        super(message);
    }
}
