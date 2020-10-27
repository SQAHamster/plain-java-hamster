package de.unistuttgart.iste.rss.oo.hamstersimulator.server.delta;

import de.unistuttgart.iste.rss.oo.hamstersimulator.server.delta.type.DeltaType;

import java.io.Serializable;

public abstract class Delta implements Serializable {
    private final DeltaType type;

    protected Delta(final DeltaType type) {
        this.type = type;
    }
}
