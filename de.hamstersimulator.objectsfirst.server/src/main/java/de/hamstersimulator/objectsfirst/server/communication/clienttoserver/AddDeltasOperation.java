package de.hamstersimulator.objectsfirst.server.communication.clienttoserver;

import de.hamstersimulator.objectsfirst.server.communication.Operation;
import de.hamstersimulator.objectsfirst.server.datatypes.delta.Delta;

import java.util.List;

import static de.hamstersimulator.objectsfirst.utils.Preconditions.checkNotNull;

/**
 * Operation to notify the server that a list of deltas should be added to the deltasList
 */
public class AddDeltasOperation implements Operation {
    private static final long serialVersionUID = -6664716881340223286L;

    /**
     * the deltas to add
     */
    private final List<Delta> deltaList;

    /**
     * Creates a new AddDeltasOperation
     * @param deltaList the deltas to add, must be != null, might be empty
     */
    public AddDeltasOperation(final List<Delta> deltaList) {
        checkNotNull(deltaList);

        this.deltaList = deltaList;
    }

    /**
     * Getter for the deltaList
     * @return the list with the deltas to add, != null, might be empty
     */
    public List<Delta> getDeltaList() {
        return deltaList;
    }
}
