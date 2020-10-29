package de.unistuttgart.iste.rss.oo.hamstersimulator.server.internal;

import de.unistuttgart.iste.rss.oo.hamstersimulator.server.delta.Delta;

import java.util.List;

public class GameStatus {
    private final GameState state;
    private final List<Delta> deltas;

    public GameStatus(final GameState state, final List<Delta> deltas) {
        this.state = state;
        this.deltas = deltas;
    }
}
