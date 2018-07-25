package de.unistuttgart.iste.rss.oo.hamstersimulator.commands;

public abstract class Command {
    public abstract void execute();
    public abstract void undo();
}
