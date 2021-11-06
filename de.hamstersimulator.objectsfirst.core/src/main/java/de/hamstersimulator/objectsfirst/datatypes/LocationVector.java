package de.hamstersimulator.objectsfirst.datatypes;

/**
 * A location vector is a vector in the mathematical sense. It can be used to describe a location
 * relative to another location.
 *
 * @author Steffen Becker
 *
 */
public final class LocationVector {

    /**
     * The vector's delta in the row number. Can be any integer number.
     */
    private final int deltaRow;

    /**
     * The vector's delta in the column number. Can be any integer number.
     */
    private final int deltaColumn;

    /**
     * Creates a new location vector with the given vector deltas.
     *
     * @param newDeltaRow
     *            The delta in the row count. Can be any integer.
     * @param newDeltaColumn
     *            The delta in the column count. Can be any integer.
     */
    public LocationVector(final int newDeltaRow, final int newDeltaColumn) {
        super();
        this.deltaRow = newDeltaRow;
        this.deltaColumn = newDeltaColumn;
    }

    /**
     * @return The delta of the row this vector describes.
     */
    public /* @ pure; helper @ */ int getDeltaRow() {
        return deltaRow;
    }

    /**
     * @return The delta of the column this vector describes.
     */
    public /* @ pure; helper @ */ int getDeltaColumn() {
        return deltaColumn;
    }

    /**
     * Returns a scaled version of this vector. Both
     * axis are scaled by the same scalar.
     * @param scale integer scalar to use for scaling this vector
     * @return A new scaled LocationVector
     */
    LocationVector scale(final int scale) {
        return new LocationVector(this.deltaRow * scale, this.deltaColumn * scale);
    }
    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + deltaRow;
        result = prime * result + deltaColumn;
        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final LocationVector other = (LocationVector) obj;
        if (deltaRow != other.deltaRow) {
            return false;
        }
        if (deltaColumn != other.deltaColumn) {
            return false;
        }
        return true;
    }
}
