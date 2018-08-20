package de.unistuttgart.iste.rss.oo.hamstersimulator.commands;

public abstract class Command {

    public final static Command EMPTY = new Command() {
        @Override
        protected void execute() {}
        
        @Override
        protected void undo() {}
    };
    
    protected abstract void execute();
    protected abstract void undo();

}
