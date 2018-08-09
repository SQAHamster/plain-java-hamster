package de.unistuttgart.iste.rss.oo.hamstersimulator.commands;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.specification.CommandSpecification;

public interface CommandInterface<T extends CommandSpecification> {
    public T getSpecification();
}