package de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.territory.command.specification;

import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.command.specification.territory.ObservableInitializeTerritoryCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.CommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Size;

public final class InitializeTerritoryCommandSpecification implements CommandSpecification, ObservableInitializeTerritoryCommandSpecification {

    private final Size size;

    public InitializeTerritoryCommandSpecification(final Size size) {
        super();
        this.size = size;
    }

    @Override
    public Size getSize() {
        return this.size;
    }
}
