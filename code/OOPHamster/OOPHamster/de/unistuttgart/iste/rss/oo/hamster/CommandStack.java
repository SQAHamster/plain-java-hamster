package de.unistuttgart.iste.rss.oo.hamster;

import java.util.LinkedList;
import java.util.List;

import de.unistuttgart.iste.rss.oo.hamster.commands.Command;

public class CommandStack {

    private final HamsterSimulator simulator;
    private final List<Command> executedCommands = new LinkedList<>();

    public CommandStack(final HamsterSimulator hamsterSimulator) {
        super();
        this.simulator = hamsterSimulator;
    }

    public void execute(final Command command) {
        command.execute();
        this.executedCommands.add(command);
    }

}
