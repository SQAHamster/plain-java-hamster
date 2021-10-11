package de.hamstersimulator.objectsfirst.server.http.server;

import com.sun.net.httpserver.HttpExchange;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static de.hamstersimulator.objectsfirst.utils.Preconditions.checkNotNull;

/**
 * Wrapper for a HTTP request which provides the query parameters
 * and encapsulates the result and statusCode
 */
public class RequestContext {

    //@ public instance invariant result != null;

    private final Map<String, String> parameters;

    /**
     * the result sent back to the client, always != null
     */
    private String result = "";

    /**
     * the status code for the result, should be a valid HTTP status code
     */
    private int statusCode = 200;

    /**
     * Creates a new RequestContext instance, automatically extracts the query
     * parameters from the exchange
     * By default, the statusCode is 200 and the result an empty String
     * @param exchange the exchange provided by the HTTPServer
     */
    public RequestContext(final HttpExchange exchange) {
        parameters = new HashMap<>();
        final String query = exchange.getRequestURI().getQuery();
        if (query != null) {
            for (String param : query.split("&")) {
                String[] entry = param.split("=");
                if (entry.length > 1) {
                    parameters.put(entry[0], entry[1]);
                } else {
                    parameters.put(entry[0], "");
                }
            }
        }
    }

    /*@
     @ requires true;
     @ pure;
     @*/
    /**
     * Gets a query parameter
     * @param parameter the name of the parameter (case sensitive)
     * @return the parameter if found, otherwise an empty optional
     */
    public Optional<String> getQueryParam(final String parameter) {
        return Optional.ofNullable(parameters.get(parameter));
    }

    /*@
     @ requires result != null;
     @ ensures getResult() == result;
     @*/
    /**
     * Sets the result of the request
     * @param result the result, should not be too long, must be != null
     */
    public void setResult(final String result) {
        checkNotNull(result);

        this.result = result;
    }

    /*@
     @ requires true;
     @ ensures getStatusCode() == statusCode;
     */
    /**
     * Sets the statusCode for the result
     * @param statusCode the code, should be a valid HTTP status code
     */
    public void setStatusCode(final int statusCode) {
        this.statusCode = statusCode;
    }

    /*@
     @ requires true;
     @ pure;
     @ ensures \result != null;
     @*/
    /**
     * Getter for the result
     * @return the result, != null, might be empty
     */
    public String getResult() {
        return result;
    }

    /*@
     @ requires true;
     @ pure;
     @*/
    /**
     * Getter for the statusCode
     * @return the status code, this should be a valid HTTP status code, however this is not enforced
     */
    public int getStatusCode() {
        return statusCode;
    }
}
