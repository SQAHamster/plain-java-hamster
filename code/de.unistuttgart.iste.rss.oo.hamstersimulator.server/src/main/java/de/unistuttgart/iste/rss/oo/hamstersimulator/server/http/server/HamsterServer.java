package de.unistuttgart.iste.rss.oo.hamstersimulator.server.http.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import static de.unistuttgart.iste.rss.utils.Preconditions.checkArgument;
import static de.unistuttgart.iste.rss.utils.Preconditions.checkNotNull;

/**
 * Base class for the HamsterHTTPServer, handles the session management and accepts new clients
 */
class HamsterServer {

    /**
     * the serverSocket which accepts new hamster clients
     */
    private final ServerSocket serverSocket;

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
     * Creates a new HamsterServer which listens for new hamsterClients via the the serverSocket
     *
     * @param serverSocket          the server socket used to accept hamster client tcp connections
     */
    protected HamsterServer(final ServerSocket serverSocket) {
        checkNotNull(serverSocket);
        checkArgument(!serverSocket.isClosed());

        this.serverSocket = serverSocket;
        startListenForSessions(serverSocket);
        startLifetimeRefreshTimer();
    }

    /*@
     @ requires serverSocket != null;
     @ requires !serverSocket.isClosed();
     @*/
    /**
     * starts the tcp server which listens for hamster clients
     * This starts another thread, which is shutdown when the serverSocket is closed
     *
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
        timer.scheduleAtFixedRate(task, 0, 5000);
    }

        /*@
     @ requires true;
     @ ensures sessions.containsKey(sessionId);
     @*/
    /**
     * Removes the session with the specified id.
     * If this was the last session, it shuts the server down
     *
     * @param sessionId the id of the session to remove
     */
    protected void removeSession(final int sessionId) {
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
     * Also shuts all sessions down
     */
    protected void shutdown() {
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            // ignore
        }
        this.sessions.values().forEach(HamsterSession::shutdown);
    }

    /**
     * Getter for the sessions map
     * @return an unmodifiable version of the sessions map
     */
    protected Map<Integer, HamsterSession> getSessions() {
        return Collections.unmodifiableMap(this.sessions);
    }
}
