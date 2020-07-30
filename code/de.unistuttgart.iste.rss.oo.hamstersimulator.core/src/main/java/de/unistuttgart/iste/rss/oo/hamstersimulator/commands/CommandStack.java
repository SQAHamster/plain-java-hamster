package de.unistuttgart.iste.rss.oo.hamstersimulator.commands;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;

public abstract class CommandStack {

    protected final ReadOnlyBooleanWrapper canUndo = new ReadOnlyBooleanWrapper(this, "canUndo", false);
    protected final ReadOnlyBooleanWrapper canRedo = new ReadOnlyBooleanWrapper(this, "canRedo", false);

    protected final List<Command> executedCommands = new LinkedList<>();
    protected final Stack<Command> undoneCommands = new Stack<>();

    public CommandStack() {
        super();
    }

    public void execute(final Command command) {
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
        final Command undoneCommand = this.executedCommands.remove(this.executedCommands.size()-1);
        undoneCommand.undo();
        undoneCommands.push(undoneCommand);
        this.canUndo.set(executedCommands.size() > 0);
        this.canRedo.set(true);
    }

    public void redo() {
        final Command undoneCommand = this.undoneCommands.pop();
        undoneCommand.execute();
        this.executedCommands.add(undoneCommand);
        this.canUndo.set(true);
        this.canRedo.set(undoneCommands.size() > 0);
    }

    /*@
     @ requires true;
     @ ensures !this.canUndo.get();
     @ ensures !this.canRedo.get();
     @ ensures this.executedCommands.isEmpty();
     @ ensures this.undoneCommands.isEmpty();
     @*/
    /**
     * hard-resets the CommandStack. it clears executedCommands and undoneCommands, however, it does NOT
     * undo all commands. If this behaviour is desired, it is necessary to call undoAll first
     */
    public void hardReset() {
        this.executedCommands.clear();
        this.undoneCommands.clear();
        this.canUndo.set(false);
        this.canRedo.set(false);
    }

    public ReadOnlyBooleanProperty canUndoProperty() {
        return this.canUndo.getReadOnlyProperty();
    }

    public ReadOnlyBooleanProperty canRedoProperty() {
        return this.canRedo.getReadOnlyProperty();
    }

}
