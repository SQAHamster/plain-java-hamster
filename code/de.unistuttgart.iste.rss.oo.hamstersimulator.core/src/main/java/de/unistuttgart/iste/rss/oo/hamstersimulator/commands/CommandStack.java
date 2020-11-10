package de.unistuttgart.iste.rss.oo.hamstersimulator.commands;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;

import static de.unistuttgart.iste.rss.utils.Preconditions.checkState;

public abstract class CommandStack {

    protected final ReadOnlyBooleanWrapper canUndo = new ReadOnlyBooleanWrapper(this, "canUndo", false);
    protected final ReadOnlyBooleanWrapper canRedo = new ReadOnlyBooleanWrapper(this, "canRedo", false);

    protected final List<Command> executedCommands = new LinkedList<>();
    protected final Stack<Command> undoneCommands = new Stack<>();
    private final ReentrantLock stateLock = new ReentrantLock(true);

    public CommandStack() {
        super();
    }

    public void execute(final Command command) {
        getStateLock().lock();
        try {
            redoAll();
            command.execute();
            this.executedCommands.add(command);
            this.canUndo.set(true);
        } finally {
            getStateLock().unlock();
        }
    }

    /*@
     @ true;
     @ ensures !canUndoProperty().get();
     @*/
    /**
     * Undoes all commands which are executed, in the inverse order than they where
     * executed
     */
    public void undoAll() {
        getStateLock().lock();
        try {
            while (canUndo.get()) {
                undo();
            }
        } finally {
            getStateLock().unlock();
        }
    }

    /*@
     @ requires true;
     @ ensures !canRedoProperty().get();
     @*/
    /**
     * Redoes all commands which are currently undone, in the same order
     * like they where executed first
     */
    public void redoAll() {
        getStateLock().lock();
        try {
            while (canRedo.get()) {
                redo();
            }
        } finally {
            getStateLock().unlock();
        }
    }

    /*@
     @ requires canUndoProperty().get();
     @ ensures !canUndoProperty().get();
     @ ensures canRedoProperty().get();
     @*/
    /**
     * Undoes the last executed command, if possible
     * @throws IllegalStateException if !canUndoProperty().get();
     */
    public void undo() {
        getStateLock().lock();
        try {
            checkState(canUndo.get(), "Cannot undo");

            final Command undoneCommand = this.executedCommands.remove(this.executedCommands.size()-1);
            undoneCommand.undo();
            undoneCommands.push(undoneCommand);
            this.canUndo.set(executedCommands.size() > 0);
            this.canRedo.set(true);
        } finally {
            getStateLock().unlock();
        }
    }

    /*@
     @ requires canRedoProperty().get();
     @ ensures !canRedoProperty().get();
     @ ensures canUndoProperty().get();
     @*/
    /**
     * Redoes the oldest undone command, if possible
     * @throws IllegalStateException if !canRedoProperty().get();
     */
    public void redo() {
        getStateLock().lock();
        try {
            checkState(canRedo.get(), "Cannot redo");

            final Command undoneCommand = this.undoneCommands.pop();
            undoneCommand.execute();
            this.executedCommands.add(undoneCommand);
            this.canUndo.set(true);
            this.canRedo.set(undoneCommands.size() > 0);
        } finally {
            getStateLock().unlock();
        }
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
        getStateLock().lock();
        try {
            this.executedCommands.clear();
            this.undoneCommands.clear();
            this.canUndo.set(false);
            this.canRedo.set(false);
        } finally {
            getStateLock().unlock();
        }
    }

    /**
     * Getter for the canUndo property
     * true if the game is stopped / paused and there is at least one command to undo
     * @return the property (not null)
     */
    public ReadOnlyBooleanProperty canUndoProperty() {
        return this.canUndo.getReadOnlyProperty();
    }

    /**
     * Getter for the canRedo property
     * true if the game is stopped / paused and there is at least one command to redo
     * @return the property (not null)
     */
    public ReadOnlyBooleanProperty canRedoProperty() {
        return this.canRedo.getReadOnlyProperty();
    }

    /**
     * Gets a lock used to synchronize all state (mode, canUndo, canRedo) related methods
     * @return the lock (always the same instance)
     */
    protected Lock getStateLock() {
        return this.stateLock;
    }

}
