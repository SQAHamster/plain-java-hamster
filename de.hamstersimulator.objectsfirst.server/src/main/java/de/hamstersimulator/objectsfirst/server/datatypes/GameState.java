package de.unistuttgart.iste.sqa.oo.hamstersimulator.server.datatypes;

import de.unistuttgart.iste.sqa.oo.hamstersimulator.datatypes.Mode;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.server.datatypes.delta.Delta;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.server.input.InputMessage;

import java.util.List;

import static de.unistuttgart.iste.sqa.utils.Preconditions.checkArgument;
import static de.unistuttgart.iste.sqa.utils.Preconditions.checkNotNull;

/**
 * Wrapper class for the state of a game with some deltas, used for json serialization
 */
public class GameState {
    /**
     * the current mode of the game
     */
    private final Mode mode;
    /**
     * the current input message, null if no input is requested
     */
    private final InputMessage inputMessage;
    /**
     * true if undo is possible
     */
    private final boolean canUndo;
    /**
     * true if redo is possible
     */
    private final boolean canRedo;
    /**
     * the current speed of the game, in range [0, 10]
     */
    private final double speed;
    /**
     * a list with some deltas
     */
    private final List<Delta> deltas;
    /**
     * the id / index of the first delta, null if no delta is provided
     */
    private final Integer firstDeltaId;

    /**
     * Creates a new GameState which should only be used for serialization
     * @param mode the current mode of the game, must be != null
     * @param inputMessage the input message if input is requested, otherwise null
     * @param canUndo true if undo is possible
     * @param canRedo true if redo is possible
     * @param speed the current speed, must be in range [0, 10]
     * @param deltas a sublist of all deltas, must be != null
     * @param firstDeltaId the id / index of the first provided delta, ignored if no delta is provided
     */
    public GameState(final Mode mode, final InputMessage inputMessage,
                     final boolean canUndo, final boolean canRedo, final double speed,
                     final List<Delta> deltas, final int firstDeltaId) {
        checkNotNull(mode);
        checkArgument(speed >= 0 && speed <= 10, "Provided speed is not in range [0, 10]");
        checkNotNull(deltas);

        this.mode = mode;
        this.inputMessage = inputMessage;
        this.canUndo = canUndo;
        this.canRedo = canRedo;
        this.speed = speed;
        this.deltas = deltas;
        this.firstDeltaId = deltas.isEmpty() ? null : firstDeltaId;
    }
}
