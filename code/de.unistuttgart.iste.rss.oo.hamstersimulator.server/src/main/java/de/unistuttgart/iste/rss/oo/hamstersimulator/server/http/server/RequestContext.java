package de.unistuttgart.iste.rss.oo.hamstersimulator.server.http.server;

import com.sun.net.httpserver.HttpExchange;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class RequestContext {

    private final Map<String, String> parameters;

    private String result = "";

    private int statusCode = 200;

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

    public Optional<String> getQueryParam(final String parameter) {
        return Optional.ofNullable(parameters.get(parameter));
    }

    public void setResult(final String result) {
        this.result = result;
    }

    public void setStatusCode(final int statusCode) {
        this.statusCode = statusCode;
    }

    public String getResult() {
        return result;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
