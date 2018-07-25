package de.unistuttgart.iste.rss.oo.hamstersimulator.commands;

import java.util.LinkedList;
import java.util.List;

import de.unistuttgart.iste.rss.oo.hamstersimulator.HamsterSimulator;

public class CommandStack {

    private final List<Command> executedCommands = new LinkedList<>();

    public CommandStack(final HamsterSimulator hamsterSimulator) {
        super();
    }

    public void execute(final Command command) {
        command.execute();
        this.executedCommands.add(command);
    }

}
