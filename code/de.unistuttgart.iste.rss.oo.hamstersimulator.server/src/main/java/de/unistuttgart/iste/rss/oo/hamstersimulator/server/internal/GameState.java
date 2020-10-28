package de.unistuttgart.iste.rss.oo.hamstersimulator.server.internal;

import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Mode;


public class GameState {
    private final Mode mode;
    private final InputMessage inputMessage;
    private final boolean canUndo;
    private final boolean canRedo;
    private final double speed;

    public GameState(final Mode mode, final InputMessage inputMessage,
                     final boolean canUndo, final boolean canRedo, final double speed) {
        this.mode = mode;
        this.inputMessage = inputMessage;
        this.canUndo = canUndo;
        this.canRedo = canRedo;
        this.speed = speed;
    }
}
