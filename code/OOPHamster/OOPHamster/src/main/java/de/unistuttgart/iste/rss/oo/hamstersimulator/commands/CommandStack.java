package de.unistuttgart.iste.rss.oo.hamstersimulator.commands;

import java.util.ArrayList;
import java.util.Collections;
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

        delay();

    }

    public void delay() {
        try {
            Thread.sleep(1000);
        } catch (final InterruptedException e) { }
    }

    public void undoAll() {
        final List<Command> reversedList = new ArrayList<>(executedCommands);
        Collections.reverse(reversedList);
        reversedList.stream().forEach(command -> {command.undo(); delay();});
        this.executedCommands.clear();
    }

}
