package de.unistuttgart.iste.rss.oo.hamstersimulator.server.datatypes;

import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Mode;
import de.unistuttgart.iste.rss.oo.hamstersimulator.server.datatypes.delta.Delta;

import java.util.List;


public class GameState {
    private final Mode mode;
    private final InputMessage inputMessage;
    private final boolean canUndo;
    private final boolean canRedo;
    private final double speed;
    private final List<Delta> deltas;

    public GameState(final Mode mode, final InputMessage inputMessage,
                     final boolean canUndo, final boolean canRedo, final double speed,
                     final List<Delta> deltas) {
        this.mode = mode;
        this.inputMessage = inputMessage;
        this.canUndo = canUndo;
        this.canRedo = canRedo;
        this.speed = speed;
        this.deltas = deltas;
    }
}
