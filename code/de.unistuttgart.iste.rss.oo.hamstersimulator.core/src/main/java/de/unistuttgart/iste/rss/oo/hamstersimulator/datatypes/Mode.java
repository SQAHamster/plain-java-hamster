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
    PAUSED
}
