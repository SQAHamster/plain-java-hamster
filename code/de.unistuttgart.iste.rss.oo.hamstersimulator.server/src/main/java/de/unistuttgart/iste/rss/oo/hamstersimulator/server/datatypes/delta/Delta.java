package de.unistuttgart.iste.rss.oo.hamstersimulator.server.datatypes.delta;

import de.unistuttgart.iste.rss.oo.hamstersimulator.server.datatypes.type.DeltaType;

import java.io.Serializable;

public abstract class Delta implements Serializable {
    private final DeltaType type;

    protected Delta(final DeltaType type) {
        this.type = type;
    }
}
