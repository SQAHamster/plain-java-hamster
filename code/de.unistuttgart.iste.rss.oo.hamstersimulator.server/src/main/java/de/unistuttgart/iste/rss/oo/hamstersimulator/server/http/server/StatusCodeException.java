package de.unistuttgart.iste.rss.oo.hamstersimulator.server.http.server;

public class StatusCodeException extends RuntimeException {
    private final int statusCode;

    public StatusCodeException(final int statusCode, final String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
