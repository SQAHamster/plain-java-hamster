package de.unistuttgart.iste.rss.oo.hamstersimulator.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class CommandStack <CommandType extends CommandInterface> {

    private final List<CommandType> executedCommands = new LinkedList<>();

    public CommandStack() {
        super();
    }

    public void execute(final CommandType command) {
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
        final List<CommandType> reversedList = new ArrayList<>(executedCommands);
        Collections.reverse(reversedList);
        reversedList.stream().forEach(command -> command.undo());
        this.executedCommands.clear();
    }

}
