package de.unistuttgart.iste.rss.oo.hamstersimulator.server.http.server;


import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class RequestHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println(exchange.getRequestURI().getPath());
        System.out.println(exchange.getRequestURI().getQuery());
        System.out.println(exchange.getRequestMethod());
    }
}
