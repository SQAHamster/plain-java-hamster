package de.unistuttgart.iste.rss.oo.hamster;

public class Territory {

    private final Tile[][] tiles;
    private Integer rowCount;
    private Integer columnCount;

    /**
     *
     * @param territoryFileName
     */
    public Territory(final String territoryFileName) {
        super();
        this.tiles = new Tile[rowCount][columnCount];
    }

    public Integer getRowCount() {
        return this.rowCount;
    }

    public Integer getColumnCount() {
        return this.columnCount;
    }

    public Tile[][] getTiles() {
        return this.tiles;
    }

    public Tile getTileAt(final Location location) {
        return tiles[location.getRow()][location.getColumn()];
    }

}