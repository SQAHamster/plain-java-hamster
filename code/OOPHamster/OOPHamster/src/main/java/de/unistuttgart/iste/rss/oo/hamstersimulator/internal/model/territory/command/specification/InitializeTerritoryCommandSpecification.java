package de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.territory.command.specification;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.specification.CommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Size;

public final class InitializeTerritoryCommandSpecification implements CommandSpecification {

    private final Size size;

    public InitializeTerritoryCommandSpecification(final Size size) {
        super();
        this.size = size;
    }

    public Size getSize() {
        return size;
    }
}
