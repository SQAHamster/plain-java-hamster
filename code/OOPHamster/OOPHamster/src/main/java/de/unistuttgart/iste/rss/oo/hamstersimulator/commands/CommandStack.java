package de.unistuttgart.iste.rss.oo.hamstersimulator.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public abstract class CommandStack <T extends Command> {

    protected final List<T> executedCommands = new LinkedList<>();

    public CommandStack() {
        super();
    }

    public void execute(final T command) {
        command.execute();
        this.executedCommands.add(command);

        delay();

    }

    public void delay() {
        try {
            Thread.sleep(200);
        } catch (final InterruptedException e) { }
    }

    public void undoAll() {
        final List<T> reversedList = new ArrayList<>(executedCommands);
        Collections.reverse(reversedList);
        reversedList.stream().forEach(command -> command.undo());
        this.executedCommands.clear();
    }

}
