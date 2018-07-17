package de.unistuttgart.iste.rss.oo.hamster;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.regex.Pattern;

public class Territory {

    private static final int DEFAULT_HAMSTER_ID = -1;
    private final Tile[][] tiles;
    private int rowCount;
    private int columnCount;
    private HamsterSimulator simulator;

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

    private void buildTerritoryFromString(final String string) {
        final Pattern lineSplitter = Pattern.compile("\n|\r\n");
        final String[] lines = lineSplitter.split(string);
        this.columnCount = Integer.parseInt(lines[0]);
        this.rowCount = Integer.parseInt(lines[1]);
        final String[] territoryDefinition = Arrays.copyOfRange(lines,2,this.rowCount+2);
        buildTiles(territoryDefinition);

        for (int i = 0; i < cornPosition.size(); i++) {
            final int[] p = (int[]) cornPosition.get(i);
            final int count = Integer.parseInt(lines[2 + height + i]);
            corn[p[0]][p[1]] = count;
        }
        defaultHamster.setMouth(Integer.parseInt(lines[2 + height
                                                       + cornPosition.size()]));

    }

    private void buildTiles(final String[] lines) {
        final LinkedList<Location> cornLocations = new LinkedList<Location>();
        Hamster defaultHamster;
        for (int i = 0; i < this.rowCount; i++) {
            for (int j = 0; j < this.columnCount; j++) {
                final Location currentLocation = new Location(j,i);
                switch (lines[i + 2].charAt(j)) {
                case '#':
                    this.tiles[i][j] = Tile.createWall(currentLocation);
                    break;
                case ' ':
                    break;
                case '*':
                    cornLocations.add(currentLocation);
                    break;
                case '^':
                    cornLocations.add(currentLocation);
                    defaultHamster = new Hamster(simulator, DEFAULT_HAMSTER_ID, currentLocation, Direction.NORTH);
                    break;
                case '>':
                    cornLocations.add(currentLocation);
                    defaultHamster = new Hamster(simulator, DEFAULT_HAMSTER_ID, currentLocation, Direction.EAST);
                    break;
                case 'v':
                    cornLocations.add(currentLocation);
                    defaultHamster = new Hamster(simulator, DEFAULT_HAMSTER_ID, currentLocation, Direction.SOUTH);
                    break;
                case '<':
                    cornLocations.add(currentLocation);
                    defaultHamster = new Hamster(simulator, DEFAULT_HAMSTER_ID, currentLocation, Direction.WEST);
                    break;
                default:
                    throw new RuntimeException("Territory error.");
                }
            }
        }
        simulator.
    }

}