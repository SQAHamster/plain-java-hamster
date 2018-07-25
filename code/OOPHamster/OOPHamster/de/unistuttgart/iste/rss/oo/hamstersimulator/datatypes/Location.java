package de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes;

public final class Location {

    @Override
    public String toString() {
        return "Location [row=" + row + ", column=" + column + "]";
    }

    private final int row;
    private final int column;

    public Location(final int row, final int column) {
        super();
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + column;
        result = prime * result + row;
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
        final Location other = (Location) obj;
        if (column != other.column) {
            return false;
        }
        if (row != other.row) {
            return false;
        }
        return true;
    }

    public Location translate(final LocationVector movementVector) {
        return new Location(this.row + movementVector.getDeltaRow(), this.column + movementVector.getDelteColumn());
    }

}