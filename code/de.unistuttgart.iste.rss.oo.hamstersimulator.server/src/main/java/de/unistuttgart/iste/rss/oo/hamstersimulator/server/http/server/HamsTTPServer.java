package de.unistuttgart.iste.rss.oo.hamstersimulator.server.http.server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;
import de.unistuttgart.iste.rss.oo.hamstersimulator.server.input.InputMessage;

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

import static de.unistuttgart.iste.rss.utils.Preconditions.*;

/**
 * The Server of the HamsterGame
 * Hamster clients can connect to the server via TCP, it also serves requests from UIs via HTTP
 * Sessions (hamster client) are automatically removed as they are closed, and this server shuts down as soon as
 * the last session is closed.
 */
public class HamsTTPServer {
    /**
     * port of the HTTP webserver
     */
    private final int httpServerPort;
    /**
     * address of the HTTP webserver
     * normally localhost, however another address can be used
     * if the server should be reachable from outside
     */
    private final InetAddress httpServerInetAddress;

    /**
     * the serverSocket which accepts new hamster clients
     */
    private final ServerSocket serverSocket;
    /**
     * HTTP server which handles HTTP requests from UIs
     */
    private final HttpServer httpServer;
    /**
     * map with all sessions (connected hamster clients)
     */
    private final Map<Integer, HamsterSession> sessions = new ConcurrentHashMap<>();

    /*@
     @ requires serverSocket != null;
     @ requires !serverSocket.isClosed();
     @ requires httpServerInetAddress != null;
     @ requires (httpServerPort > 0) && (httpServerPort <= 65535);
     @*/
    /**
     * Creates a new HamsTTPServer which listens for new hamsterClients via the the serverSocket
     * and listens for http requests on port httpServerPort
     * @param serverSocket the server socket used to accept hamster client tcp connections
     * @param httpServerInetAddress the address on which the http server listens for requests
     * @param httpServerPort the port on which the http server should listen for requests
     * @throws IOException if it is not possible to start the http server
     */
    private HamsTTPServer(final ServerSocket serverSocket, final InetAddress httpServerInetAddress, final int httpServerPort) throws IOException {
        checkNotNull(serverSocket);
        checkNotNull(httpServerInetAddress);
        checkArgument(!serverSocket.isClosed());
        checkArgument((httpServerPort > 0 && httpServerPort <= 65535));

        this.httpServerInetAddress = httpServerInetAddress;
        this.httpServerPort = httpServerPort;
        this.serverSocket = serverSocket;
        startListenForSessions(serverSocket);
        this.httpServer = createHttpServer();
        startLifetimeRefreshTimer();
    }

    /*@
     @ requires true;
     @*/
    /**
     * Starts the HamsTTPServer with the default port and address if possible
     * if the server is already running, it does nothing
     * The default port for the TCP server is 8008, the default address for the HTTP server
     * is 127.0.0.1 and the default port for the HTTP server is 8080
     */
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

    /*@
     @ requires httpServerInetAddress != null;
     @ requires (httpServerPort > 0) && (httpServerPort <= 65535);
     @ requires (port > 0) && (port <= 65535);
     @*/
    /**
     * Starts the HamsTTPServer on the specified port and address
     * If it is not possible to start the server, an IOException is thrown
     * Use with care! Most UIs probably expect the HamsterServer to be running on the default port and address.
     * @param httpServerInetAddress the address on which the HTTP server should listen for requests.
     *                              e.g. 0.0.0.0 to listen on all network interfaces or 127.0.0.1 to listen
     *                              on localhost
     * @param httpServerPort the port on which the HTTP server should listen for requests
     * @param port the port on which the TCP server listens for hamster clients
     * @throws IOException if one of the ports is already blocked
     * @throws IllegalArgumentException if port or httpServerPort is no allowed TCP port
     */
    public static void startOnPort(final InetAddress httpServerInetAddress, final int httpServerPort,
                                         final int port) throws IOException {
        checkNotNull(httpServerInetAddress);
        checkArgument((httpServerPort > 0) && (httpServerPort <= 65535));
        checkArgument((port > 0) && (port <= 65535));

        final ServerSocket serverSocket = new ServerSocket(port);
        new HamsTTPServer(serverSocket, httpServerInetAddress, httpServerPort);
    }

    /*@
     @ requires serverSocket != null;
     @ requires !serverSocket.isClosed();
     @*/
    /**
     * starts the tcp server which listens for hamster clients
     * This starts another thread, which is shutdown when the serverSocket is closed
     * @param serverSocket the server socket used to accept new hamster clients
     *                     for each client, a new HamsterSession is created and added
     * @throws IllegalArgumentException if the serverSocket is already closed
     */
    private void startListenForSessions(final ServerSocket serverSocket) {
        checkNotNull(serverSocket);
        checkArgument(!serverSocket.isClosed());

        new Thread(() -> {
            try {
                int sessionIdCounter = 0;
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

    /*@
     @ requires true;
     @*/
    /**
     * Creates and starts the http server which handles the UI's HTTP requests.
     * This is non-blocking
     * @return the HttpServer which is already started
     * @throws IOException if it is not possible to start the server, for example
     *                     if the port is already blocked
     */
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

    /*@
     @ requires true;
     @*/
    /**
     * Starts a timer which repeatedly invokes shutdownIfPossible on all
     * sessions and removes them if they aren't alive any longer
     */
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


    /*@
     @ requires context != null;
     @*/
    /**
     * Handles the state HTTP GET request
     * @param context the request context
     */
    private void getState(final RequestContext context) {
        checkNotNull(context);

        final HamsterSession session = getSession(context);
        final int since = getIntQueryParam(context, "since");

        final Gson gson = new Gson();
        context.setResult(gson.toJson(session.getGameState(since)));
    }

    /*@
     @ requires context != null;
     @*/
    /**
     * Handles the gamesList HTTP GET request
     * @param context the request context
     */
    private void getGamesList(final RequestContext context) {
        checkNotNull(context);

        final Gson gson = new Gson();
        context.setResult(gson.toJson(this.sessions.keySet()));
    }

    /*@
     @ requires context != null;
     @*/
    /**
     * Handles the input HTTP POST request
     * @param context the request context
     */
    private void postInput(final RequestContext context) {
        checkNotNull(context);

        final HamsterSession session = getSession(context);
        final int inputId = getIntQueryParam(context, "inputId");
        final String input = getQueryParam(context, "input");
        final Optional<InputMessage> inputMessage = session.getInputMessage();
        if (inputMessage.isEmpty() || (inputId != inputMessage.get().getInputId())) {
            throw new StatusCodeException(400, "outdated inputId");
        }

        session.setInputResult(inputId, input);
    }

    /*@
     @ requires context != null;
     @*/
    /**
     * Handles the action HTTP POST request
     * @param context the request context
     */
    private void postAction(final RequestContext context) {
        checkNotNull(context);

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

    /*@
     @ requires context != null;
     @*/
    /**
     * Handles the speed HTTP POST request
     * @param context the request context
     */
    private void postSpeed(final RequestContext context) {
        checkNotNull(context);

        final HamsterSession session = getSession(context);
        final double speed = getDoubleQueryParam(context, "speed");

        if (speed < 0 || speed > 10) {
            throw new StatusCodeException(400, "Provided speed is not in range [0, 10]");
        }
        session.changeSpeed(speed);
    }

    /*@
     @ requires context != null;
     @ requires parameter != null;
     @*/
    /**
     * Gets a string query parameter from the specified context and throws a StatusCodeException if
     * the parameter is not provided
     * @param context the RequestContext which contains the query parameter
     * @param parameter the parameter to get
     * @throws StatusCodeException if the parameter is not set
     * @return the value of the parameter
     */
    private String getQueryParam(final RequestContext context, final String parameter) {
        checkNotNull(context);
        checkNotNull(parameter);

        final Optional<String> queryParam = context.getQueryParam(parameter);
        if (queryParam.isEmpty()) {
            throw new StatusCodeException(400, "necessary query parameter not provided: " + parameter);
        } else {
            return queryParam.get();
        }
    }

    /*@
     @ requires context != null;
     @ requires parameter != null;
     @*/
    /**
     * Gets an int query parameter from the specified context and throws a StatusCodeException if
     * the parameter is not provided or if the provided value is not a valid int
     * @param context the RequestContext which contains the query parameter
     * @param parameter the parameter to get
     * @throws StatusCodeException if the parameter is not set
     * @return the value of the parameter
     */
    private int getIntQueryParam(final RequestContext context, final String parameter) {
        checkNotNull(context);
        checkNotNull(parameter);

        final String queryParam = getQueryParam(context, parameter);
        try {
            return Integer.parseInt(queryParam);
        } catch (NumberFormatException e) {
            throw new StatusCodeException(400, "illegal query parameter format: " + parameter + " was no legal int");
        }
    }

    /*@
     @ requires context != null;
     @ requires parameter != null;
     @*/
    /**
     * Gets an double query parameter from the specified context and throws a StatusCodeException if
     * the parameter is not provided or if the provided value is not a valid double
     * @param context the RequestContext which contains the query parameter
     * @param parameter the parameter to get
     * @throws StatusCodeException if the parameter is not set
     * @return the value of the parameter
     */
    private double getDoubleQueryParam(final RequestContext context, final String parameter) {
        checkNotNull(context);
        checkNotNull(parameter);

        final String queryParam = getQueryParam(context, parameter);
        try {
            return Double.parseDouble(queryParam);
        } catch (NumberFormatException e) {
            throw new StatusCodeException(400, "illegal query parameter format: " + parameter + " was no legal double");
        }
    }

    /*@
     @ requires context != null;
     @*/
    /**
     * Gets the session based on the id query parameter.
     * If no id parameter is provided or if the session with the id does not
     * exist, a StatusCodeException is thrown
     * @param context the RequestContext which contains the id query parameter
     * @throws StatusCodeException if the session does not exist or the id parameter is not provided
     * @return the session
     */
    private HamsterSession getSession(final RequestContext context) {
        checkNotNull(context);

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

    /*@
     @ requires true;
     @ ensures sessions.containsKey(sessionId);
     @*/
    /**
     * Removes the session with the specified id.
     * If this was the last session, it shuts the server down
     * @param sessionId the id of the session to remove
     */
    private void removeSession(final int sessionId) {
        sessions.remove(sessionId);
        if (sessions.size() == 0) {
            shutdown();
        }
    }

    /*@
     @ requires true;
     @ ensures serverSocket.isClosed();
     @*/
    /**
     * Shuts this server down.
     * Closes the server socket which accepts new clients
     * and stops the HTTP server.
     * Also shuts all sessions down
     */
    private void shutdown() {
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            // ignore
        }
        this.sessions.values().forEach(HamsterSession::shutdown);
        this.httpServer.stop(0);
    }

}
