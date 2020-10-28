package de.unistuttgart.iste.rss.oo.hamstersimulator.server.communication.clienttoserver;

import de.unistuttgart.iste.rss.oo.hamstersimulator.server.communication.Operation;

public class CanRedoChangedOperation implements Operation {
    private static final long serialVersionUID = -6385372283517596119L;
    private final boolean canRedo;
    
    public CanRedoChangedOperation(final boolean canRedo) {
        this.canRedo = canRedo;
    }

    public boolean isCanRedo() {
        return canRedo;
    }
}
