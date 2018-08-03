package de.unistuttgart.iste.rss.oo.hamstersimulator.commands;

public abstract class EntityCommand<T> extends Command {
    protected final PropertyMap<T> entityState;

    protected EntityCommand(final PropertyMap<T> entityState) {
        super();
        this.entityState = entityState;
    }
}
