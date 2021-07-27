package de.unistuttgart.iste.rss.oo.hamstersimulator.adapter;

import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.ObservableLog;
import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.ObservableTerritory;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static de.unistuttgart.iste.rss.utils.Preconditions.checkNotNull;

/**
 * Provides all components of a game which are necessary to attach a UI or a test framework to a game
 * <ul>
 *     <li>territory: makes it possible to bind to the territory and observe changes on it</li>
 *     <li>controller: makes it possible to control the associated game: stop, pause etc.</li>
 *     <li>log: makes it possible to display log messages</li>
 * </ul>
 * Also supports adding inputInterfaces via <code>addInputInterface</code>.<br>
 * Besides inputInterfaces and the controller, this class only provides read-only access.
 * In particular it is not possible to modify the territory or anything on it or add log messages.
 */
public class HamsterGameViewModel {
    /**
     * The territory object on which the instance of this game takes place on
     */
    private final ObservableTerritory territory;

    /**
     * Provides methods to control the hamster game: stop, pause, resume, undo, redo etc.
     */
    private final HamsterGameController gameController;

    /**
     * The log which contains all log messages.
     */
    private final ObservableLog log;

    /**
     * The input interfaces used when hamsters ask for input.
     */
    private final Set<InputInterface> inputInterfaces;

    /*@
     @ requires territory != null;
     @ requires gameController != null;
     @ requires log != null;
     @ requires inputInterfaces != null;
     @*/
    /**
     * Creates a new HamsterGameViewModel
     * This is only meant for internal use
     * @param territory the territory, must be != null
     * @param gameController the gameController to stop, pause etc., must be != null
     * @param log the log with all log messages, must be != null
     * @param inputInterfaces an initial collection of InputInterfaces, must be != null, might be empty
     */
    public HamsterGameViewModel(final ObservableTerritory territory, final HamsterGameController gameController,
                                final ObservableLog log, Collection<InputInterface> inputInterfaces) {
        checkNotNull(territory, "territory must be != null");
        checkNotNull(gameController, "gameController must be != null");
        checkNotNull(log, "log must be != null");
        checkNotNull(inputInterfaces, "inputInterfaces must be != null");

        this.territory = territory;
        this.gameController = gameController;
        this.log = log;
        this.inputInterfaces = new HashSet<>(inputInterfaces);
    }

    /*@
     @ requires true;
     @ ensures \result != null;
     @ pure;
     @*/
    /**
     * Getter for the territory
     * This can be used to observe changes of the territory or objects on it (e.g. hamsters)
     * @return The territory (not null)
     */
    public ObservableTerritory getTerritory() {
        return this.territory;
    }

    /*@
     @ requires true;
     @ ensures \result != null;
     @ pure;
     @*/
    /**
     * Getter for the gameController
     * This can be used to control the associated game: stop, pause, resume, undo, redo etc.
     * @return The gameController associated with this game (not null)
     */
    public HamsterGameController getGameController() {
        return this.gameController;
    }

    /*@
     @ requires true;
     @ ensures \result != null;
     @ pure;
     @*/
    /**
     * Getter for the log
     * This can be used to get the log entries
     * @return The ObservableLog associated with this game (not null)
     */
    public ObservableLog getLog() {
        return this.log;
    }

    /*@
     @ requires true;
     @ ensures \result != null;
     pure;
     @*/
    /**
     * Gets the input interfaces of this game used to read values from users
     * or mock objects.
     * @return An unmodifiable set which contains all current input interfaces
     */
    public Set<InputInterface> getInputInterfaces() {
        return Collections.unmodifiableSet(this.inputInterfaces);
    }

    /*@
     @ requires newInputInterface != null;
     @ ensures getInputInterfaces().contains(newInputInterface);
     @*/
    /**
     * Adds newInputInterface to this game's input interfaces for reading values from users or
     * mock objects.
     * Duplicates are ignored
     * @param newInputInterface The new input interface, must be != null.
     */
    public void addInputInterface(final InputInterface newInputInterface) {
        checkNotNull(newInputInterface);
        this.inputInterfaces.add(newInputInterface);
    }

    /*@
     @ requires inputInterfaceToRemove != null;
     @ ensures !getInputInterfaces().contains(inputInterfaceToRemove);
     @*/
    /**
     * Removes inputInterfaceToRemove from this game's input interfaces for reading values from users or
     * mock objects
     * @param inputInterfaceToRemove The input interface to remove, must be != null
     */
    public void removeInputInterface(final InputInterface inputInterfaceToRemove) {
        checkNotNull(inputInterfaceToRemove, "inputInterfaceToRemove must be != null");
        this.inputInterfaces.remove(inputInterfaceToRemove);
    }
}
