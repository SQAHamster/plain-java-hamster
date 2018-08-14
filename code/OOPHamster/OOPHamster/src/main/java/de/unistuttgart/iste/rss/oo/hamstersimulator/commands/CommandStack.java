package de.unistuttgart.iste.rss.oo.hamstersimulator.commands;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;

public abstract class CommandStack <T extends Command> {

    protected final ReadOnlyBooleanWrapper canUndo = new ReadOnlyBooleanWrapper(this, "canUndo", false);
    protected final ReadOnlyBooleanWrapper canRedo = new ReadOnlyBooleanWrapper(this, "canRedo", false);

    protected final List<T> executedCommands = new LinkedList<>();
    protected final Stack<T> undoneCommands = new Stack<>();

    public CommandStack() {
        super();
    }

    public void execute(final T command) {
        redoAll();
        command.execute();
        this.executedCommands.add(command);
        this.canUndo.set(true);
    }

    public void undoAll() {
        while (canUndo.get()) {
            undo();
        }
    }

    public void redoAll() {
        while (canRedo.get()) {
            redo();
        }
    }

    public void undo() {
        final T undoneCommand = this.executedCommands.remove(this.executedCommands.size()-1);
        undoneCommand.undo();
        undoneCommands.push(undoneCommand);
        this.canUndo.set(executedCommands.size() > 0);
        this.canRedo.set(true);
    }

    public void redo() {
        final T undoneCommand = this.undoneCommands.pop();
        undoneCommand.execute();
        this.executedCommands.add(undoneCommand);
        this.canUndo.set(true);
        this.canRedo.set(undoneCommands.size() > 0);
    }

    public ReadOnlyBooleanProperty canUndoProperty() {
        return this.canUndo.getReadOnlyProperty();
    }

    public ReadOnlyBooleanProperty canRedoProperty() {
        return this.canRedo.getReadOnlyProperty();
    }

}
