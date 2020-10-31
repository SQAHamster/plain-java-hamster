package de.unistuttgart.iste.rss.oo.hamstersimulator.adapter;

import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Mode;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyObjectProperty;

/**
 * Makes it possible to control the execution of the associated hamster game
 * supported features are
 * <ul>
 *     <li>undo(All), redo(All)</li>
 *     <li>speed</li>
 *     <li>pause(Async), resume, stop</li>
 *     <li>mode (read-only)</li>
 * </ul>
 */
public interface HamsterGameController {
    /*@
     @ true;
     @ ensures !canUndoProperty().get();
     @*/
    /**
     * Undoes all commands which are executed, in the inverse order than they where
     * executed
     */
    void undoAll();

    /*@
     @ requires true;
     @ ensures !canRedoProperty().get();
     @*/
    /**
     * Redoes all commands which are currently undone, in the same order
     * like they where executed first
     */
    void redoAll();

    /*@
     @ requires canUndoProperty().get();
     @ ensures !canUndoProperty().get();
     @ ensures canRedoProperty().get();
     @*/
    /**
     * Undoes the last executed command, if possible
     * @throws IllegalStateException if !canUndoProperty().get();
     */
    void undo();

    /*@
     @ requires canRedoProperty().get();
     @ ensures !canRedoProperty().get();
     @ ensures canUndoProperty().get();
     @*/
    /**
     * Redoes the oldest undone command, if possible
     * @throws IllegalStateException if !canRedoProperty().get();
     */
    void redo();

    /*@
     @ requires speed >= 0 && speed <= MAX_SPEED;
     @ ensures getSpeed() == speed;
     @*/
    /**
     * Set the speed of the hamster game. Valid values are in the range from
     * 0.0 to 10.0,
     * where 0.0 is slow and 10.0 is fast.
     * @param speed The new game speed's delay. Has to be greater or equal 0.0 and
     *                  less than or equal 10.0.
     */
    void setSpeed(final double speed);

    /*@
     @ requires true;
     @ ensures modeProperty().get() == Mode.STOPPED;
     */
    /**
     * Stop the execution of the game. The game is finished and needs to be reset / hardReset
     * or closed.
     * If the game is already stopped, this does nothing
     */
    void stopGame();

    /*@
     @ requires true;
     @ ensures (\old(modeProperty().get()) == Mode.STOPPED) ==> (modeProperty().get() == Mode.STOPPED);
     @ ensures (\old(modeProperty().get()) == Mode.PAUSED) ==> (modeProperty().get() == Mode.ABORTED);
     @ ensures (\old(modeProperty().get()) == Mode.RUNNING) ==> (modeProperty().get() == Mode.ABORTED);
     @ ensures (\old(modeProperty().get()) == Mode.INITIALIZING) ==> (modeProperty().get() == Mode.STOPPED);
     @ ensures (\old(modeProperty().get()) == Mode.ABORTED) ==> (modeProperty().get() == Mode.ABORTED);
     */
    /**
     * Abort or stop the execution of the game. The game is finished and needs to be reset / hardReset
     * or closed.
     * If the game is running, paused or aborted, the game is aborted.
     * if the game is initializing or stopped, it is stopped.
     */
    void abortOrStopGame();

    /*@
     @ requires modeProperty().get() == Mode.RUNNING;
     @ ensures modeProperty().get() == Mode.PAUSED;
     @*/
    /**
     * Pauses the game when it is running.
     * If the game is not running (paused previously, not started or stopped), an exception
     * is thrown.
     * This is executed synchronously, this methods returns after the game was successfully paused.
     * @throws IllegalStateException if modeProperty().get() != Mode.RUNNING
     */
    void pause();

    /*@
     @ requires modeProperty().get() == Mode.RUNNING;
     @*/
    /**
     * Pauses the game when it is running.
     * If the game is not running (paused previously, not started or stopped), an exception
     * is thrown.
     * This is executed asynchronously, this methods returns before the game was paused.
     * Use with care! Normally, this is only necessary for UI functionality.
     * @throws IllegalStateException if modeProperty().get() != Mode.RUNNING
     */
    void pauseAsync();

    /*@
     @ requires modeProperty().get() == Mode.PAUSED;
     @ ensures modeProperty().get() == Mode.RUNNING;
     @*/
    /**
     * Pauses the HamsterGame.
     * It is only possible to execute this in paused mode.
     *
     * @throws IllegalStateException if modeProperty().get() != Mode.PAUSED
     */
    void resume();

    /**
     * Getter for the mode property
     * provides read-only access to the current mode of the associated game
     * @return the property (not null)
     */
    ReadOnlyObjectProperty<Mode> modeProperty();

    /**
     * Getter for the speed property
     * provides read and write access to the current speed of the associated game
     * Note: if this property is set to an invalid value (not in range [0, 10]), an exception
     * is thrown
     *
     * @return the property (not null)
     */
    DoubleProperty speedProperty();

    /**
     * Getter for the canUndo property
     * true if the game is stopped / paused and there is at least one command to undo
     * @return the property (not null)
     */
    ReadOnlyBooleanProperty canUndoProperty();

    /**
     * Getter for the canRedo property
     * true if the game is stopped / paused and there is at least one command to redo
     * @return the property (not null)
     */
    ReadOnlyBooleanProperty canRedoProperty();
}
