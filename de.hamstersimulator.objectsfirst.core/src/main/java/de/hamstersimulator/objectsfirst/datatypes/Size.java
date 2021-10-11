package de.hamstersimulator.objectsfirst.datatypes;

/**
 * Objects of the class Size represent any kind of thing having a
 * two dimensional size. It is used in the context of the hamster
 * simulator as a measure of the hamster's territories extend
 * (which again limits the amount of tiles the hamster can move).
 * @author Steffen Becker
 *
 */
public final class Size {

    /**
     * Number of (max.) columns in this size object.
     */
    private final int columnCount;

    /**
     * Number of (max.) rows in this size object.
     */
    private final int rowCount;

    /**
     * Create a new size object with the given dimensions.
     * @param newRowCount Number of rows of this size object.
     * @param newColumnCount Number of columns in this size object.
     */
    public Size(final int newRowCount, final int newColumnCount) {
        super();
        this.columnCount = newColumnCount;
        this.rowCount = newRowCount;
    }

    /**
     * @return The column size part of this object.
     */
    public int getColumnCount() {
        return columnCount;
    }

    /**
     * @return The row size part of this object.
     */
    public int getRowCount() {
        return rowCount;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + columnCount;
        result = prime * result + rowCount;
        return result;
    }

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
        final Size other = (Size) obj;
        if (columnCount != other.columnCount) {
            return false;
        }
        if (rowCount != other.rowCount) {
            return false;
        }
        return true;
    }

}
