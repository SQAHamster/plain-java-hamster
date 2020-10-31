package de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes;

/**
 * The modes a hamster game can be in
 */
public enum Mode {
    /**
     * The game runs, new commands can be executed.
     * It is possible to pause the game or to stop it.
     * It is not possible to load a new territory.
     */
    RUNNING,
    /**
     * Default mode before the territory is loaded.
     * It is possible to load other territories.
     * To execute commands, it is necessary to call startGame.
     */
    INITIALIZING,
    /**
     * The game was stopped on purpose or an exception occurred which stopped the game.
     * It is possible to undo / redo commands, but it is not possible to execute new commands.
     * It is also not possible to load another territory.
     */
    STOPPED,
    /**
     *  It is necessary to continue the game to execute new commands.
     *  It is possible to resume the game or to stop it.
     *  It is not possible to load a new territory.
     */
    PAUSED,
    /**
     * The game was aborted via api or mainly UI
     * like STOPPED, however it throws an Exception which is not caught by doRun or runGame
     * After one executed command, this Mode is automatically converted to STOPPED
     */
    ABORTED;

    /**
     * Checks if the mode is STOPPED or ABORTED
     * @return true if this is stopped or aborted
     */
    public boolean isStoppedOrAborted() {
        return this == STOPPED || this == ABORTED;
    }
}
