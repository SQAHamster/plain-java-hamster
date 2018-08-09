package de.unistuttgart.iste.rss.oo.hamstersimulator.simulator.commands;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.CommandInterface;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.specification.CommandSpecification;

public interface GameCommand<T extends CommandSpecification> extends CommandInterface<T> {
}
