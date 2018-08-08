package de.unistuttgart.iste.rss.oo.hamstersimulator.commands;

public abstract class EntityCommand<T> implements CommandInterface {

    protected PropertyMap<T> entityState;

    protected EntityCommand(final PropertyMap<T> entityState) {
        super();
        this.entityState = entityState;
    }

}
