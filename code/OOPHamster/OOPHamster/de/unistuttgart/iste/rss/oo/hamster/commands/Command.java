package de.unistuttgart.iste.rss.oo.hamster.commands;

public abstract class Command {
    public abstract void execute();
    public abstract void undo();
}
