package de.unistuttgart.iste.sqa.oo.hamstersimulator.testframework.gamestate;

import de.unistuttgart.iste.sqa.utils.Preconditions;

/**
 * Objects of this class describe the amount of grains picked up or dropped
 * as a consequence of executing commands.
 * @author Steffen Becker
 *
 */
final class GrainDelta {

    /**
     * Constant to be used if no grains have been picked up or dropped.
     */
    static final GrainDelta NO_CHANGE = new GrainDelta(0, 0);

    /**
     * Amount of additional grains picked up.
     */
    private final int deltaPickedGrains;

    /**
     * Amount of grains dropped.
     */
    private final int deltaDroppedGrains;

    /**
     * Create a new grain delta object.
     * @param newDeltaPickedGrains Number of grains picked up in the last command. Has to be >= 0.
     * @param newDeltaDroppedGrains Number of grains dropped in the last command. Has to be >= 0.
     */
    GrainDelta(final int newDeltaPickedGrains, final int newDeltaDroppedGrains) {
        super();
        Preconditions.checkArgument(newDeltaPickedGrains >= 0);
        Preconditions.checkArgument(newDeltaDroppedGrains >= 0);
        this.deltaPickedGrains = newDeltaPickedGrains;
        this.deltaDroppedGrains = newDeltaDroppedGrains;
    }

    /**
     * @return the deltaPickedGrains
     */
    public int getDeltaPickedGrains() {
        return deltaPickedGrains;
    }

    /**
     * @return the deltaDroppedGrains
     */
    public int getDeltaDroppedGrains() {
        return deltaDroppedGrains;
    }

}
