package de.unistuttgart.iste.rss.oo.hamstersimulator.commands;

public abstract class Command {

    protected abstract void execute();

    protected abstract void undo();

}
