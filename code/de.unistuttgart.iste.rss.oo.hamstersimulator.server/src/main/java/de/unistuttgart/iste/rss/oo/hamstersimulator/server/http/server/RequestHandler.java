package de.unistuttgart.iste.rss.oo.hamstersimulator.server.http.server;


import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class RequestHandler implements HttpHandler {

    private final Map<String, Consumer<RequestContext>> getHandlers = new ConcurrentHashMap<>();
    private final Map<String, Consumer<RequestContext>> postHandlers = new ConcurrentHashMap<>();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        final RequestContext context = new RequestContext(exchange);
        try {
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
        } catch (StatusCodeException e) {
            context.setResult(e.getMessage());
            context.setStatusCode(e.getStatusCode());
        } catch (Exception e) {
            context.setResult(e.getMessage());
            context.setStatusCode(300);
        }
        exchange.sendResponseHeaders(context.getStatusCode(), context.getResult().getBytes().length);
        final OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(context.getResult().getBytes());
        outputStream.close();
    }

    public void get(final String path, final Consumer<RequestContext> handler) {
        getHandlers.put(path, handler);
    }

    public void post(final String path, final Consumer<RequestContext> handler) {
        postHandlers.put(path, handler);
    }
}
