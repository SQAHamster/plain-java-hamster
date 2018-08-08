package de.unistuttgart.iste.rss.oo.hamstersimulator.commands;

public interface CommandInterface {
    public abstract void execute();
    public abstract void undo();
}