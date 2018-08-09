package de.unistuttgart.iste.rss.oo.hamstersimulator.commands;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.specification.PropertyCommandSpecification;

public abstract class EntityCommand<T> extends AbstractBaseCommand<PropertyCommandSpecification> implements CommandInterface<PropertyCommandSpecification> {

    protected PropertyMap<T> entityState;

    protected EntityCommand(final PropertyMap<T> entityState, final PropertyCommandSpecification specification) {
        super(specification);
        this.entityState = entityState;
    }

}
