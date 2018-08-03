package de.unistuttgart.iste.rss.oo.hamstersimulator.commands;

public interface InjectableCommand<T> {

    void setContext(PropertyMap<T> territoryState);

}
