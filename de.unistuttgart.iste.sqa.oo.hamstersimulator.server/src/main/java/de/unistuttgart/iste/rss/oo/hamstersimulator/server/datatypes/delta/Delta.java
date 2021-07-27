package de.unistuttgart.iste.rss.oo.hamstersimulator.server.datatypes.delta;

import de.unistuttgart.iste.rss.oo.hamstersimulator.server.datatypes.type.DeltaType;

import java.io.Serializable;

import static de.unistuttgart.iste.sqa.utils.Preconditions.checkNotNull;

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
