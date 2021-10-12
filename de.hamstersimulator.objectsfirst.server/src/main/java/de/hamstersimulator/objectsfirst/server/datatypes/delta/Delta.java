package de.hamstersimulator.objectsfirst.server.datatypes.delta;

import de.hamstersimulator.objectsfirst.server.datatypes.type.DeltaType;

import java.io.Serializable;

import static de.hamstersimulator.objectsfirst.utils.Preconditions.checkNotNull;

/**
 * Base class for all deltas
 */
public abstract class Delta implements Serializable {
    /**
     * the type of this delta
     */
    private final DeltaType type;

    /**
     * Creates a new Delta with the specified type
     * @param type the type of this delta, must be != null
     */
    protected Delta(final DeltaType type) {
        checkNotNull(type);

        this.type = type;
    }
}
