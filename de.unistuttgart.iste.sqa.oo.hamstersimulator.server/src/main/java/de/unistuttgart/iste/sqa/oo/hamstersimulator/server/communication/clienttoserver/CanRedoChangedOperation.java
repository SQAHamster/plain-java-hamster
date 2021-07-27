package de.unistuttgart.iste.sqa.oo.hamstersimulator.server.communication.clienttoserver;

import de.unistuttgart.iste.sqa.oo.hamstersimulator.server.communication.Operation;

/**
 * Operation to notify the server that canUndo changed
 */
public class CanRedoChangedOperation implements Operation {
    private static final long serialVersionUID = -6385372283517596119L;
    private final boolean canRedo;

    /**
     * Creates a new CanRedoChangedOperation
     * @param canRedo the new value of canRedo
     */
    public CanRedoChangedOperation(final boolean canRedo) {
        this.canRedo = canRedo;
    }

    /**
     * Getter for the new value of canRedo
     * @return the new value for canRedo
     */
    public boolean isCanRedo() {
        return canRedo;
    }
}
