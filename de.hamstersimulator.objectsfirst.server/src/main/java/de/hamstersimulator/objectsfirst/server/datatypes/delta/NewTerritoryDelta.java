package de.hamstersimulator.objectsfirst.server.datatypes.delta;

import de.hamstersimulator.objectsfirst.datatypes.Size;
import de.hamstersimulator.objectsfirst.server.datatypes.type.DeltaType;

import static de.hamstersimulator.objectsfirst.utils.Preconditions.checkNotNull;

/**
 * Delta which represents a new territory
 */
public class NewTerritoryDelta extends Delta {
    private static final long serialVersionUID = 1272925783034654704L;

    /**
     * the width of the new territory
     */
    private final int width;
    /**
     * the height of the new territory
     */
    private final int height;

    /**
     * Creates a new NewTerritoryDelta
     * @param size the size of the new territory, must eb != null
     */
    public NewTerritoryDelta(final Size size) {
        super(DeltaType.NEW_TERRITORY);
        checkNotNull(size);

        this.width = size.getColumnCount();
        this.height = size.getRowCount();
    }
}
