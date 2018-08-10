package de.unistuttgart.iste.rss.oo.hamstersimulator.commands;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

import java.util.Collection;

import com.google.common.collect.Lists;

import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.InitHamsterCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.MoveCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.PickGrainCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.PutGrainCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.TurnLeftCommand;

public class GameCommandStack<T extends Command> extends CommandStack<T> {

    public enum Mode {
        RUNNING, INITIALIZING, STOPPED
    }

    private Mode currentMode = Mode.INITIALIZING;
    private final Collection<Class<?>> allowedGameCommands = Lists.newArrayList(MoveCommand.class, PickGrainCommand.class, PutGrainCommand.class, TurnLeftCommand.class, InitHamsterCommand.class);

    public void startGame() {
        currentMode = Mode.RUNNING;
        this.executedCommands.clear();
    }

    public void stopGame() {
        currentMode = Mode.STOPPED;
    }

    @Override
    public void execute(final T command) {
        checkState(!(currentMode == Mode.STOPPED));
        checkArgument(currentMode == Mode.INITIALIZING || allowedGameCommands.contains(command.getClass()), "Only game commands may be executed in game mode!");
        super.execute(command);
    }

}
