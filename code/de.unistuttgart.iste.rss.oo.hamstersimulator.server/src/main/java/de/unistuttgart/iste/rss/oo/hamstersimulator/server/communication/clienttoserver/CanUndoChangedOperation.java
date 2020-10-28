package de.unistuttgart.iste.rss.oo.hamstersimulator.server.communication.clienttoserver;

import de.unistuttgart.iste.rss.oo.hamstersimulator.server.communication.Operation;

public class CanUndoChangedOperation implements Operation {
    private static final long serialVersionUID = -4879144763702849063L;
    private final boolean canUndo;

    public CanUndoChangedOperation(final boolean canUndo) {
        this.canUndo = canUndo;
    }

    public boolean isCanUndo() {
        return canUndo;
    }
}
