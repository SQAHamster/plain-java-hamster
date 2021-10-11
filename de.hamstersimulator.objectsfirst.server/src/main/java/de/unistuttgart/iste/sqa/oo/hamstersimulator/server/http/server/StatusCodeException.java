package de.unistuttgart.iste.sqa.oo.hamstersimulator.server.http.server;

/**
 * Exception used to to also provide a status code
 * for the HTTP request
 */
public class StatusCodeException extends RuntimeException {
    private final int statusCode;

    /**
     * Creates a new StatusCodeException with the provided statusCode and the message
     * @param statusCode the status code, should be a valid HTTP status code
     * @param message the message sent to the client, should not contain sensitive data
     */
    public StatusCodeException(final int statusCode, final String message) {
        super(message);
        this.statusCode = statusCode;
    }

    /**
     * Getter for the statusCode
     * @return gets the status code, probably a valid HTTP status code
     */
    public int getStatusCode() {
        return statusCode;
    }
}
