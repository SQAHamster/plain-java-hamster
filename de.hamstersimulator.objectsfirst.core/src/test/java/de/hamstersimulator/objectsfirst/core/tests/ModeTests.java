package de.hamstersimulator.objectsfirst.core.tests;

import de.hamstersimulator.objectsfirst.commands.Command;
import de.hamstersimulator.objectsfirst.commands.GameCommandStack;
import de.hamstersimulator.objectsfirst.datatypes.Mode;
import de.hamstersimulator.objectsfirst.exceptions.GameAbortedException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ModeTests {

    @Test
    public void testStopWhileRunning() {
        final GameCommandStack commandStack = new GameCommandStack();
        commandStack.startGame(false);
        assertEquals(Mode.RUNNING, commandStack.modeProperty().get());

        commandStack.stopGame();
        assertEquals(Mode.STOPPED, commandStack.modeProperty().get());
        final Command command = getCommand();
        assertThrows(IllegalStateException.class, () -> commandStack.execute(command));
        assertEquals(Mode.STOPPED, commandStack.modeProperty().get());
    }

    @Test
    public void testAbortWhileRunning() {
        final GameCommandStack commandStack = new GameCommandStack();
        commandStack.startGame(false);
        assertEquals(Mode.RUNNING, commandStack.modeProperty().get());

        commandStack.abortOrStopGame();
        assertEquals(Mode.ABORTED, commandStack.modeProperty().get());
        final Command command = getCommand();
        assertThrows(GameAbortedException.class, () -> commandStack.execute(command));
        assertEquals(Mode.STOPPED, commandStack.modeProperty().get());
    }

    @Test
    public void testStopWhilePaused() {
        final GameCommandStack commandStack = new GameCommandStack();
        commandStack.startGame(true);
        assertEquals(Mode.PAUSED, commandStack.modeProperty().get());

        commandStack.stopGame();
        assertEquals(Mode.STOPPED, commandStack.modeProperty().get());
        final Command command = getCommand();
        assertThrows(IllegalStateException.class, () -> commandStack.execute(command));
        assertEquals(Mode.STOPPED, commandStack.modeProperty().get());
    }

    @Test
    public void testAbortWhilePaused() {
        final GameCommandStack commandStack = new GameCommandStack();
        commandStack.startGame(true);
        assertEquals(Mode.PAUSED, commandStack.modeProperty().get());

        commandStack.abortOrStopGame();
        assertEquals(Mode.ABORTED, commandStack.modeProperty().get());
        final Command command = getCommand();
        assertThrows(GameAbortedException.class, () -> commandStack.execute(command));
        assertEquals(Mode.STOPPED, commandStack.modeProperty().get());
    }

    @Test
    public void testAbortWhileStopped() {
        final GameCommandStack commandStack = new GameCommandStack();
        commandStack.startGame(false);
        commandStack.stopGame();
        assertEquals(Mode.STOPPED, commandStack.modeProperty().get());

        commandStack.abortOrStopGame();
        assertEquals(Mode.STOPPED, commandStack.modeProperty().get());
        final Command command = getCommand();
        assertThrows(IllegalStateException.class, () -> commandStack.execute(command));
        assertEquals(Mode.STOPPED, commandStack.modeProperty().get());
    }

    @Test
    public void testAbortWhileInitializing() {
        final GameCommandStack commandStack = new GameCommandStack();
        assertEquals(Mode.INITIALIZING, commandStack.modeProperty().get());

        commandStack.abortOrStopGame();
        assertEquals(Mode.STOPPED, commandStack.modeProperty().get());
        final Command command = getCommand();
        assertThrows(IllegalStateException.class, () -> commandStack.execute(command));
        assertEquals(Mode.STOPPED, commandStack.modeProperty().get());
    }

    private Command getCommand() {
        final Command command = Mockito.mock(Command.class);
        return command;
    }
}
