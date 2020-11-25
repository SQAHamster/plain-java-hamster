package de.unistuttgart.iste.rss.oo.hamstersimulator.testframework.gamestate;

import de.unistuttgart.iste.rss.utils.Preconditions;

final class GrainDelta {

    /**
     * Constant to be used if no grains have been picked up or dropped.
     */
    static final GrainDelta NO_CHANGE = new GrainDelta(0, 0);

    /**
     * Amount of additional grain picked up.
     */
    private final int deltaPickedGrains;

    /**
     * Amount of grain dropped.
     */
    private final int deltaDroppedGrains;

    /**
     * Create a new grain delta object.
     * @param newDeltaPickedGrains Number of grains picked up in the last command. Has to be >= 0.
     * @param deltaDroppedGrains Number of grains dropped in the last command. Has to be >= 0.
     */
    GrainDelta(final int newDeltaPickedGrains, final int deltaDroppedGrains) {
        super();
        Preconditions.checkArgument(newDeltaPickedGrains >= 0);
        Preconditions.checkArgument(deltaDroppedGrains >= 0);
        this.deltaPickedGrains = newDeltaPickedGrains;
        this.deltaDroppedGrains = deltaDroppedGrains;
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
