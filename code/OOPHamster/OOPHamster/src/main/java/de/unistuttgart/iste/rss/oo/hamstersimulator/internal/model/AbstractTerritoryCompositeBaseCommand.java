package de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.AbstractCompositeCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.specification.CommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.specification.SpecifiedCommand;

abstract class AbstractTerritoryCompositeBaseCommand<T extends CommandSpecification> extends AbstractCompositeCommand implements SpecifiedCommand<T> {

    protected final Territory territory;
    protected final T specification;

    public AbstractTerritoryCompositeBaseCommand(final Territory territory, final T spec) {
        super();
        this.territory = territory;
        this.specification = spec;
    }

    @Override
    public T getSpecification() {
        return this.specification;
    }
}
