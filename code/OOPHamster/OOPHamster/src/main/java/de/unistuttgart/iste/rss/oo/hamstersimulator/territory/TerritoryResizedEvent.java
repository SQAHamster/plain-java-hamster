package de.unistuttgart.iste.rss.oo.hamstersimulator.territory;

public class TerritoryResizedEvent extends TerritoryEvent {
    private final int columnCount;
    private final int rowCount;

    public TerritoryResizedEvent(final Territory territory, final int columnCount, final int rowCount) {
        super(territory);
        this.columnCount = columnCount;
        this.rowCount = rowCount;
    }

    public int getColumnCount() {
        return columnCount;
    }
    public int getRowCount() {
        return rowCount;
    }

}
