package de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes;

import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Immutable class representing a location in the hamster territory. Locations
 * are made of a row and a column number.
 * @author Steffen Becker
 *
 */
public final class Location {

    /**
     * Constant representing the origin (0,0) of the coordinate system. 
     */
    public static final Location ORIGIN = Location.from(0,0);

    /**
     * Variable to store the row number.
     */
    private final int row;
    
    /**
     * Variable to store the column number.
     */
    private final int column;

    /** 
     * Create a new location. The rows and column numbers of locations
     * in the hamster territory start by (0,0) and increase only to
     * positive integers. Larger rows are further down. Larger columns
     * are further right.
     * @param row The non-negative row count.
     * @param column The non-negative column count.
     */
    public Location(final int row, final int column) {
        super();
        if (row < 0 || column < 0) {
            throw new IllegalArgumentException("Negative row/column not allowed.");
        }
        this.row = row;
        this.column = column;
    }

    /**
     * @return The non-negative row number of this location.
     */
    public /*@ pure, helper @*/ int getRow() {
        return row;
    }

    /**
     * @return The non-negative column number of this location.
     */
    public /*@ pure, helper @*/ int getColumn() {
        return column;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + column;
        result = prime * result + row;
        return result;
    }

    /* (non-Javadoc)
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
        final Location other = (Location) obj;
        if (column != other.column) {
            return false;
        }
        if (row != other.row) {
            return false;
        }
        return true;
    }

    /**
     * Creates a new location object by using this location object
     * as base point and adding the given location vector to it in
     * a mathematical sense.
     * @param movementVector A {@link LocationVector} to be added to this location
     * @return A new location object which is the result of adding the given
     *      location vector to this location. Will never be null.
     */
    public /*@ pure, helper @*/ Location translate(final LocationVector movementVector) {
        return new Location(this.row + movementVector.getDeltaRow(), this.column + movementVector.getDeltaColumn());
    }

    /** 
     * Convienience method to create a new location. The rows and column numbers of locations
     * in the hamster territory start by (0,0) and increase only to
     * positive integers. Larger rows are further down. Larger columns
     * are further right.
     * @param row The non-negative row count.
     * @param column The non-negative column count.
     */    
    public static Location from(final int row, final int column) {
        return new Location(row, column);
    }

    /**
     * This method creates a {@link Stream} of locations which enumerate the locations
     * formed by the box with the from location as upper, left corner and the to location
     * as the lower, right one. The box' locations are enumerated row by row.
     * @param from Upper left corner of the box to be enumerated in the stream.
     * @param to Lower right corner of the box to be enumerated in the stream.
     * @return The stream which enumerates the locations in the specified box.
     */
    public static Stream<Location> getAllLocationsFromTo(final Location from, final Location to) {
        final Stream<Stream<Location>> stream = IntStream.range(from.getRow(), to.getRow()+1).mapToObj(row -> IntStream.range(from.getColumn(), to.getColumn()+1).mapToObj(column -> Location.from(row, column)));
        return stream.flatMap(s -> s);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Location [row=" + row + ", column=" + column + "]";
    }
}