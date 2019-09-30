package de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes;

public final class Size {

    private final int columnCount;
    private final int rowCount;

    public Size(final int rowCount, final int columnCount) {
        super();
        this.columnCount = columnCount;
        this.rowCount = rowCount;
    }

    public int getColumnCount() {
        return columnCount;
    }

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
