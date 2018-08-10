package de.unistuttgart.iste.rss.oo.hamstersimulator.commands;

// import static com.google.common.base.Preconditions.checkArgument;

public class GameCommandStack<T extends Command> extends CommandStack<T> {

    @Override
    public void execute(final T command) {
        // checkArgument(command instanceof GameCommandStack<?>, "Only game commands may be executed in game mode!");
        super.execute(command);
    }

}
