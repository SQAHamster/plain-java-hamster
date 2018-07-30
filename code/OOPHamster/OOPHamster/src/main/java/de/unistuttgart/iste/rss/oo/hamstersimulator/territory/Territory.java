package de.unistuttgart.iste.rss.oo.hamstersimulator.territory;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import de.unistuttgart.iste.rss.oo.hamstersimulator.HamsterSimulator;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Direction;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;
import de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.Hamster;

public class Territory {

    private final HamsterSimulator simulator;

    private Tile[][] tiles;
    private Collection<Hamster> otherHamsters;
    private Hamster defaultHamster;
    private final List<TerritoryListener> listeners = new LinkedList<>();

    /**
     *
     * @param territoryFileName
     */
    public Territory(final HamsterSimulator simulator) {
        super();
        this.simulator = simulator;
        this.setSize(0, 0);
    }

    public int getRowCount() {
        return this.tiles.length;
    }

    public int getColumnCount() {
        return this.tiles.length > 0 ? this.tiles[0].length : 0;
    }

    public Tile getTileAt(final Location location) {
        return tiles[location.getRow()][location.getColumn()];
    }

    public Collection<Hamster> getOtherHamsters() {
        return Collections.unmodifiableCollection(this.otherHamsters);
    }

    public Hamster getDefaultHamster() {
        return this.defaultHamster;
    }

    public void buildTerritoryFromString(final String territoryFile) {
        final List<String> list = readLinesFromTerritoryFile(territoryFile);
        final String[] lines = list.toArray(new String[]{});
        setSizeFromStrings(lines);
        final String[] territoryDefinition = Arrays.copyOfRange(lines,2,lines.length);
        buildTiles(territoryDefinition);
    }

    public Territory setSize(final int columnCount, final int rowCount) {
        if (this.tiles != null) {
            for (int row = 0; row < this.tiles.length; row++) {
                for (int column = 0; column < this.tiles[0].length; column++) {
                    notifyTileRemoved(new TileRemovedEvent(this, this.tiles[row][column]));
                    this.tiles[row][column] = null;
                }
            }
            this.tiles = null;
        }
        this.tiles = new Tile[rowCount][columnCount];
        notifyResized(new TerritoryResizedEvent(this, getColumnCount(), getRowCount()));
        for (int row = 0; row < this.tiles.length; row++) {
            for (int column = 0; column < this.tiles[0].length; column++) {
                this.tiles[row][column] = Tile.createEmptyTile(this, new Location(row, column));
                notifyTileCreated(new TileAddedEvent(this, this.tiles[row][column]));
            }
        }
        return this;
    }

    public Territory wallAt(final int row, final int column) {
        this.getTileAt(new Location(row, column)).addObjectToContent(new Wall());
        return this;
    }

    public Territory defaultHamsterAt(final int row, final int column, final Direction direction, final int grainCount) {
        if (this.defaultHamster != null) {
            this.getTileAt(defaultHamster.getCurrentPosition().get()).removeObjectFromContent(this.defaultHamster);
            this.defaultHamster = null;
        }
        this.defaultHamster = new Hamster(
                simulator,
                new Location(row, column),
                direction,
                grainCount);
        this.getTileAt(new Location(row, column)).addObjectToContent(this.defaultHamster);
        return this;
    }

    private void setSizeFromStrings(final String[] lines) {
        setSize(Integer.parseInt(lines[1]), Integer.parseInt(lines[0]));
    }

    private void buildTiles(final String[] lines) {
        final LinkedList<Location> grainLocations = new LinkedList<Location>();
        Optional<Location> defaultHamsterLocation = Optional.empty();
        Optional<Direction> defaultHamsterDirection = Optional.empty();
        for (int row = 0; row < this.getRowCount(); row++) {
            for (int column = 0; column < this.getColumnCount(); column++) {
                final Location currentLocation = new Location(row,column);
                final char tileCode = lines[row].charAt(column);
                createTileAt(currentLocation, tileCode);
                switch (tileCode) {
                case ' ':
                case '#':
                    break;
                case '*':
                    grainLocations.add(currentLocation);
                    break;
                case '^':
                    grainLocations.add(currentLocation);
                    defaultHamsterLocation = Optional.of(currentLocation);
                    defaultHamsterDirection = Optional.of(Direction.NORTH);
                    break;
                case '>':
                    grainLocations.add(currentLocation);
                    defaultHamsterLocation = Optional.of(currentLocation);
                    defaultHamsterDirection = Optional.of(Direction.EAST);
                    break;
                case 'v':
                    grainLocations.add(currentLocation);
                    defaultHamsterLocation = Optional.of(currentLocation);
                    defaultHamsterDirection = Optional.of(Direction.SOUTH);
                    break;
                case '<':
                    grainLocations.add(currentLocation);
                    defaultHamsterLocation = Optional.of(currentLocation);
                    defaultHamsterDirection = Optional.of(Direction.WEST);
                    break;
                default:
                    throw new RuntimeException("Territory error.");
                }
            }
        }
        final int initialGrainCount = Integer.parseInt(lines[this.getRowCount() + grainLocations.size()]);
        this.defaultHamster = new Hamster(
                simulator,
                defaultHamsterLocation.orElseThrow(IllegalStateException::new),
                defaultHamsterDirection.orElseThrow(IllegalStateException::new),
                initialGrainCount);
        placeGrain(lines, grainLocations);
    }

    private void createTileAt(final Location currentLocation, final char tileCode) {
        if (tileCode == '#') {
            this.tiles[currentLocation.getRow()][currentLocation.getColumn()] = Tile.createWall(this, currentLocation);
        } else {
            this.tiles[currentLocation.getRow()][currentLocation.getColumn()] = Tile.createEmptyTile(this, currentLocation);
        }
        notifyTileCreated(new TileAddedEvent(this, this.tiles[currentLocation.getRow()][currentLocation.getColumn()]));
    }

    private void placeGrain(final String[] lines, final LinkedList<Location> grainLocations) {
        for (int i = 0; i < grainLocations.size(); i++) {
            final Location location = grainLocations.get(i);
            final int count = Integer.parseInt(lines[this.getRowCount() + i]);
            putNewGrain(getTileAt(location), count);
        }
    }

    private void putNewGrain(final Tile tile, final int count) {
        for (int i = 0; i < count; i++) {
            tile.addObjectToContent(new Grain());
        }
    }

    private List<String> readLinesFromTerritoryFile(final String territoryFileName) {
        final File file = new File(territoryFileName);
        final List<String> list = new ArrayList<String>();

        try ( Scanner input = new Scanner(file) )
        {
            while (input.hasNextLine()) {
                list.add(input.nextLine());
            }
        } catch (final FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    public boolean isLocationInTerritory(final Location newHamsterPosition) {
        return newHamsterPosition.getColumn() < this.getColumnCount() &&
                newHamsterPosition.getRow() < this.getRowCount();
    }

    private void notifyResized(final TerritoryResizedEvent e) {
        for (final TerritoryListener listener : listeners) {
            listener.territoryResized(e);
        }
    }

    private void notifyTileCreated(final TileAddedEvent e) {
        for (final TerritoryListener listener : listeners) {
            listener.tileAdded(e);
        }
    }

    private void notifyTileRemoved(final TileRemovedEvent e) {
        for (final TerritoryListener listener : listeners) {
            listener.tileRemoved(e);
        }
    }

    public void addTerritoryListener(final TerritoryListener listener) {
        this.listeners.add(listener);
    }

    public void removeTerritoryListener(final TerritoryListener listener) {
        this.listeners.remove(listener);
    }
}