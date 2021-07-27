package de.unistuttgart.iste.sqa.oo.hamstersimulator.server.communication.clienttoserver;

import de.unistuttgart.iste.sqa.oo.hamstersimulator.server.communication.Operation;

/**
 * Operation to notify the server that canUndo changed
 */
public class CanUndoChangedOperation implements Operation {
    private static final long serialVersionUID = -6385372283517596119L;
    private final boolean canUndo;

    /**
     * Creates a new CanUndoChangedOperation
     * @param canUndo the new value of canUndo
     */
    public CanUndoChangedOperation(final boolean canUndo) {
        this.canUndo = canUndo;
    }

    /**
     * Getter for the new value of canUndo
     * @return the new value for canUndo
     */
    public boolean isCanUndo() {
        return canUndo;
    }
}
