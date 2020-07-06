package de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.territory;

import static de.unistuttgart.iste.rss.utils.Preconditions.checkNotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.Command;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Direction;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Size;

public class TerritoryLoader {

    private final TerritoryBuilder territoryBuilder;
    private Size loadedTerritoryDimensions;

    private TerritoryLoader(final TerritoryBuilder territoryBuilder) {
        super();
        this.territoryBuilder = territoryBuilder;
    }

    public static TerritoryLoader initializeFor(final EditorTerritory territory) {
        return new TerritoryLoader(new TerritoryBuilder(territory));
    }

    public Command loadFromResourceFile(final String territoryFile) throws IOException {
        final List<String> list = readLinesFromTerritoryResourceFile(territoryFile);
        return interpretLoadedTerritoryLines(list);
    }
    
    public Command loadFromInputStream(final InputStream inputStream) throws IOException {
        final List<String> list = readLinesFromTerritoryInputStream(inputStream);
        return interpretLoadedTerritoryLines(list);
    }

    private Command interpretLoadedTerritoryLines(final List<String> list) {
        final String[] lines = list.toArray(new String[]{});
        setSizeFromStrings(lines);
        final String[] territoryDefinition = Arrays.copyOfRange(lines,2,lines.length);
        buildTiles(territoryDefinition);

        return this.territoryBuilder.build();
    }

    private void setSizeFromStrings(final String[] lines) {
        this.loadedTerritoryDimensions = new Size(Integer.parseInt(lines[1]), Integer.parseInt(lines[0]));
        this.territoryBuilder.initializeTerritory(this.loadedTerritoryDimensions);
    }

    private void buildTiles(final String[] lines) {
        final LinkedList<Location> grainLocations = new LinkedList<Location>();
        Optional<Location> defaultHamsterLocation = Optional.empty();
        Optional<Direction> defaultHamsterDirection = Optional.empty();
        for (int row = 0; row < this.loadedTerritoryDimensions.getRowCount(); row++) {
            for (int column = 0; column < this.loadedTerritoryDimensions.getColumnCount(); column++) {
                final Location currentLocation = new Location(row,column);
                final char tileCode = lines[row].charAt(column);
                switch (tileCode) {
                case ' ':
                    break;
                case '#':
                    createWallAt(currentLocation);
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
        final int initialGrainCount = Integer.parseInt(lines[this.loadedTerritoryDimensions.getRowCount() + grainLocations.size()]);
        territoryBuilder.defaultHamsterAt(defaultHamsterLocation.get(), defaultHamsterDirection.get(), initialGrainCount);
        placeGrain(lines, grainLocations);
    }

    private List<String> readLinesFromTerritoryResourceFile(final String territoryFileName) throws IOException {
        final InputStream in = getClass().getResourceAsStream(territoryFileName);
        if (in == null) {
            throw new IOException("Unable to load the territory from the filename: " + territoryFileName);
        }
        final List<String> result = readLinesFromTerritoryInputStream(in);
        in.close();
        return result;
    }

    private List<String> readLinesFromTerritoryInputStream(final InputStream inputStream) throws IOException {
        checkNotNull(inputStream);
        final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        final List<String> list = new ArrayList<String>();

        try (Scanner input = new Scanner(reader))
        {
            while (input.hasNextLine()) {
                list.add(input.nextLine());
            }
        }

        return list;
    }

    private void placeGrain(final String[] lines, final LinkedList<Location> grainLocations) {
        for (int i = 0; i < grainLocations.size(); i++) {
            final Location location = grainLocations.get(i);
            final int count = Integer.parseInt(lines[this.loadedTerritoryDimensions.getRowCount() + i]);
            territoryBuilder.grainAt(location, count);
        }
    }
    
    private void createWallAt(final Location currentLocation) {
        this.territoryBuilder.wallAt(currentLocation);
    }

}
