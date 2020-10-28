package de.unistuttgart.iste.rss.oo.hamstersimulator.server.communication.clienttoserver;

import de.unistuttgart.iste.rss.oo.hamstersimulator.server.communication.Operation;
import de.unistuttgart.iste.rss.oo.hamstersimulator.server.delta.Delta;

import java.io.Serializable;
import java.util.List;

public class AddDeltasOperation implements Operation {
    private static final long serialVersionUID = -6664716881340223286L;

    private final List<Delta> deltaList;

    public AddDeltasOperation(final List<Delta> deltaList) {
        this.deltaList = deltaList;
    }

    public List<Delta> getDeltaList() {
        return deltaList;
    }
}
