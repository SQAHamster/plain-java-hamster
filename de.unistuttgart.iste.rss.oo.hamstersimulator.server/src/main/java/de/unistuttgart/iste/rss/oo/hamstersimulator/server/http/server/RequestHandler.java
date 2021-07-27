package de.unistuttgart.iste.rss.oo.hamstersimulator.server.http.server;


import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import static de.unistuttgart.iste.rss.utils.Preconditions.checkNotNull;

/**
 * Handles incoming requests by finding the correct handler and invoking it
 * Handlers can be registered by the get and post functions
 */
public class RequestHandler implements HttpHandler {

    private final Map<String, Consumer<RequestContext>> getHandlers = new ConcurrentHashMap<>();
    private final Map<String, Consumer<RequestContext>> postHandlers = new ConcurrentHashMap<>();

    /**
     * Handles a single request
     * @param exchange the exchange represents the request
     * @throws IOException if it is not possible to send the result
     */
    @Override
    public void handle(final HttpExchange exchange) throws IOException {
        final RequestContext context = new RequestContext(exchange);
        invokeAndCatchExceptions(exchange, context);
        Headers headers = exchange.getResponseHeaders();
        headers.add("Access-Control-Allow-Headers","x-prototype-version,x-requested-with");
        headers.add("Access-Control-Allow-Methods","GET,POST");
        headers.add("Access-Control-Allow-Origin","*");
        headers.add("Content-Type","application/json");
        exchange.sendResponseHeaders(context.getStatusCode(), context.getResult().getBytes().length);
        final OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(context.getResult().getBytes());
        outputStream.close();
    }

    /*@
     @ requires exchange != null;
     @ requires context != null;
     @*/
    /**
     * Invokes the handler and catches all exceptions, sets the result and the status
     * code based on the exception
     * @param exchange the exchange from the HttpServer
     * @param context the context used to get parameters and set the result and statusCode
     */
    private void invokeAndCatchExceptions(final HttpExchange exchange, final RequestContext context) {
        checkNotNull(exchange);
        checkNotNull(context);

        try {
            invokeHandler(exchange, context);
        } catch (StatusCodeException e) {
            context.setResult(e.getMessage());
            context.setStatusCode(e.getStatusCode());
        } catch (Exception e) {
            context.setResult(e.getMessage());
            context.setStatusCode(500);
        }
    }

    /**
     * Invokes the handler if existing, otherwise throws a not found
     * @param exchange the exchange from the HttpServer
     * @param context the context used to get parameters and set the result and statusCode
     */
    private void invokeHandler(final HttpExchange exchange, final RequestContext context) {
        Map<String, Consumer<RequestContext>> handlers;
        if (exchange.getRequestMethod().equals("GET")) {
            handlers = this.getHandlers;
        } else if (exchange.getRequestMethod().equals("POST")) {
            handlers = this.postHandlers;
        } else {
            throw new StatusCodeException(404, "not found");
        }
        final Consumer<RequestContext> handler = handlers.get(exchange.getRequestURI().getPath());
        if (handler == null) {
            throw new StatusCodeException(404, "not found");
        }
        handler.accept(context);
    }

    /*@
     @ requires path != null;
     @ requires handler != null;
     @ ensures getHandlers.get(path) == handler;
     @*/
    /**
     * Adds a handler for a get request
     * If there is already a handler present, it is overwritten
     * @param path the (complete) path of the request to handle, should start with '/'
     * @param handler the handler which is invoked with the generated RequestContext
     */
    public void get(final String path, final Consumer<RequestContext> handler) {
        checkNotNull(path);
        checkNotNull(handler);

        getHandlers.put(path, handler);
    }

    /*@
     @ requires path != null;
     @ requires handler != null;
     @ ensures postHandlers.get(path) == handler;
     @*/
    /**
     * Adds a handler for a post request
     * If there is already a handler present, it is overwritten
     * @param path the (complete) path of the request to handle, should start with '/'
     * @param handler the handler which is invoked with the generated RequestContext
     */
    public void post(final String path, final Consumer<RequestContext> handler) {
        checkNotNull(path);
        checkNotNull(handler);

        postHandlers.put(path, handler);
    }
}
