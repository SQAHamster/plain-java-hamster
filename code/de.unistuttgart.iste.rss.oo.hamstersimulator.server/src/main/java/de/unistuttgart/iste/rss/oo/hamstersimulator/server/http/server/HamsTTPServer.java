package de.unistuttgart.iste.rss.oo.hamstersimulator.server.http.server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;
import de.unistuttgart.iste.rss.oo.hamstersimulator.server.datatypes.InputMessage;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

public class HamsTTPServer {
    private final int httpServerPort;
    private final InetAddress httpServerInetAddress;

    private final ServerSocket serverSocket;
    private final HttpServer httpServer;
    private final Map<Integer, HamsterSession> sessions = new ConcurrentHashMap<>();
    private int sessionIdCounter = 0;

    public HamsTTPServer(final ServerSocket serverSocket, final InetAddress httpServerInetAddress, final int httpServerPort) throws IOException {
        this.httpServerInetAddress = httpServerInetAddress;
        this.httpServerPort = httpServerPort;
        this.serverSocket = serverSocket;
        startListenForSessions(serverSocket);
        this.httpServer = createHttpServer();
        startLifetimeRefreshTimer();
    }

    public static void startIfNotRunning() {
        try {
            final ServerSocket serverSocket = new ServerSocket(8008);
            try {
                new HamsTTPServer(serverSocket, InetAddress.getByName("127.0.0.1"), 8080);
            } catch (IOException e) {
                throw new RuntimeException("failed to start server", e);
            }
        } catch (IOException e) {
            //already running
        }
    }

    public static void startOnPort(final InetAddress httpServerInetAddress, final int httpServerPort,
                                         final int port) throws IOException {
        final ServerSocket serverSocket = new ServerSocket(port);
        new HamsTTPServer(serverSocket, httpServerInetAddress, httpServerPort);
    }

    private void startListenForSessions(final ServerSocket serverSocket) {
        new Thread(() -> {
            try {
                while (!serverSocket.isClosed()) {
                    Socket socket = serverSocket.accept();
                    final HamsterSession session = new HamsterSession(socket, sessionIdCounter);
                    this.sessions.put(sessionIdCounter, session);
                    sessionIdCounter++;
                }
            } catch (IOException e) {
                shutdown();
            }
        }).start();
    }

    private HttpServer createHttpServer() throws IOException {
        final HttpServer server = HttpServer.create(new InetSocketAddress(httpServerInetAddress, httpServerPort), 5);
        server.setExecutor(null);

        final RequestHandler handler = new RequestHandler();
        handler.get("/state", this::getState);
        handler.get("/gamesList", this::getGamesList);
        handler.post("/input", this::postInput);
        handler.post("/speed", this::postSpeed);
        handler.post("/action", this::postAction);

        server.createContext("/", handler);
        server.start();
        return server;
    }

    private void startLifetimeRefreshTimer() {
        final TimerTask task = new TimerTask() {
            @Override
            public void run() {
                sessions.values().forEach(session -> {
                    session.shutdownIfPossible();
                    if (!session.isAlive()) {
                        removeSession(session.getId());
                    }
                });
            }
        };

        final Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(task, 0, 3000);
    }


    private void getState(final RequestContext context) {
        final HamsterSession session = getSession(context);
        final int since = getIntQueryParam(context, "since");

        Gson gson = new Gson();
        context.setResult(gson.toJson(session.getGameState(since)));
    }

    private void getGamesList(final RequestContext context) {
        Gson gson = new Gson();
        context.setResult(gson.toJson(this.sessions.keySet()));
    }


    private void postInput(final RequestContext context) {
        final HamsterSession session = getSession(context);
        final int inputId = getIntQueryParam(context, "inputId");
        final String input = getQueryParam(context, "input");
        final Optional<InputMessage> inputMessage = session.getInputMessage();
        if (inputMessage.isEmpty() || (inputId != inputMessage.get().getInputId())) {
            throw new StatusCodeException(400, "outdated inputId");
        }

        session.setInputResult(inputId, input);
    }

    private void postAction(final RequestContext context) {
        final HamsterSession session = getSession(context);
        final String action = getQueryParam(context, "action");

        switch (action) {
            case "resume" -> session.resume();
            case "pause" -> session.pause();
            case "undo" -> session.undo();
            case "redo" -> session.redo();
            case "abort" -> session.abort();
            default -> throw new StatusCodeException(400, "unknown action: " + action);
        }
    }

    private void postSpeed(final RequestContext context) {
        final HamsterSession session = getSession(context);
        final double speed = getDoubleQueryParam(context, "speed");

        if (speed < 0 || speed > 10) {
            throw new StatusCodeException(400, "Provided speed is not in range [0, 10]");
        }
        session.changeSpeed(speed);
    }

    private String getQueryParam(final RequestContext context, final String parameter) {
        final Optional<String> queryParam = context.getQueryParam(parameter);
        if (queryParam.isEmpty()) {
            throw new StatusCodeException(400, "necessary query parameter not provided: " + parameter);
        } else {
            return queryParam.get();
        }
    }

    private int getIntQueryParam(final RequestContext context, final String parameter) {
        final String queryParam = getQueryParam(context, parameter);
        try {
            return Integer.parseInt(queryParam);
        } catch (NumberFormatException e) {
            throw new StatusCodeException(400, "illegal query parameter format: " + parameter + " was no legal int");
        }
    }

    private double getDoubleQueryParam(final RequestContext context, final String parameter) {
        final String queryParam = getQueryParam(context, parameter);
        try {
            return Double.parseDouble(queryParam);
        } catch (NumberFormatException e) {
            throw new StatusCodeException(400, "illegal query parameter format: " + parameter + " was no legal double");
        }
    }

    private HamsterSession getSession(final RequestContext context) {
        final int sessionId = getIntQueryParam(context, "id");
        if (this.sessions.containsKey(sessionId)) {
            final HamsterSession session = this.sessions.get(sessionId);
            if (session.isAlive()) {
                return session;
            } else {
                removeSession(sessionId);
                throw new StatusCodeException(400, "session no longer available");
            }
        } else {
            throw new StatusCodeException(400, "the provided id did not match any existing session");
        }
    }

    private void removeSession(final int sessionId) {
        sessions.remove(sessionId);
        if (sessions.size() == 0) {
            shutdown();
        }
    }

    private void shutdown() {
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            // ignore
        }
        this.httpServer.stop(0);
    }

}
