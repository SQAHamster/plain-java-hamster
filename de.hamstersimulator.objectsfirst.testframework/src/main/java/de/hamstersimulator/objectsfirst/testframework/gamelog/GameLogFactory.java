package de.hamstersimulator.objectsfirst.testframework.gamelog;

import de.hamstersimulator.objectsfirst.adapter.observables.*;
import de.hamstersimulator.objectsfirst.adapter.observables.command.specification.ObservableCommandSpecification;
import de.hamstersimulator.objectsfirst.adapter.observables.command.specification.hamster.*;
import de.hamstersimulator.objectsfirst.adapter.observables.command.specification.territory.ObservableInitializeTerritoryCommandSpecification;
import de.hamstersimulator.objectsfirst.testframework.gamelog.datatypes.*;
import de.hamstersimulator.objectsfirst.utils.LambdaVisitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Factory to create a GameLog out of an initial Territory and a list of Commands
 */
public final class GameLogFactory {

    /**
     * Dispatcher used to execute state change logic dependent on the command
     * specification executed on the previous state. The return value is not used here.
     */
    private final LambdaVisitor<ObservableCommandSpecification, LogEntry> logVisitor;

    /**
     * Used to create a data entry for a TileContent
     */
    private final LambdaVisitor<ObservableTileContent, TileContentData> tileContentVisitor;

    /**
     * The TerritoryData representing the initial Territory
     */
    private final TerritoryData territoryData;

    /**
     * List of all LogEntries, from first to last
     */
    private final List<LogEntry> logEntries = new ArrayList<>();

    /**
     * Maps hamsters to their id
     */
    private final Map<ObservableHamster, Integer> hamsterIdLookup = new HashMap<>();

    /**
     * Lock used for synchronization
     */
    private final ReentrantLock lock = new ReentrantLock(true);
    /**
     * counter for the TileContent id
     */
    private volatile int idCounter = 0;

    /**
     * Creates a new GameLogFactory which can be used to create a factory which creates
     * game log entry
     *
     * @param territory the Territory initially associated with the game to observe
     */
    public GameLogFactory(final ObservableTerritory territory) {
        super();

        this.logVisitor = new LambdaVisitor<ObservableCommandSpecification, LogEntry>()
                .on(ObservableMoveCommandSpecification.class).then(this::fromMove)
                .on(ObservableTurnLeftCommandSpecification.class).then(this::fromTurnLeft)
                .on(ObservablePutGrainCommandSpecification.class).then(this::fromPutGrain)
                .on(ObservablePickGrainCommandSpecification.class).then(this::fromPickGrain)
                .on(ObservableInitHamsterCommandSpecification.class).then(this::fromInit)
                .on(ObservableWriteCommandSpecification.class).then(this::fromWrite)
                .on(ObservableInitializeTerritoryCommandSpecification.class).then(this::fromInitTerritory);

        this.tileContentVisitor = new LambdaVisitor<ObservableTileContent, TileContentData>()
                .on(ObservableHamster.class).then(hamster -> new HamsterData(hamster.getCurrentLocation().orElseThrow(),
                        this.hamsterIdLookup.get(hamster), hamster.getDirection()))
                .on(ObservableGrain.class).then(grain -> new GrainData(grain.getCurrentLocation().orElseThrow()))
                .on(ObservableWall.class).then(wall -> new WallData(wall.getCurrentLocation().orElseThrow()));

        this.territoryData = this.createTerritoryData(territory);
    }

    /**
     * Creates a TerritoryData based on a Territory
     *
     * @param territory the Territory to transform
     * @return a TerritoryData which represents the provided Territory
     */
    private TerritoryData createTerritoryData(final ObservableTerritory territory) {
        for (final ObservableHamster hamster : territory.getHamsters()) {
            this.registerHamster(hamster);
        }
        final List<TileContentData> datas = new ArrayList<>();
        for (final ObservableTile tile : territory.tilesProperty()) {
            for (final ObservableTileContent content : tile.contentProperty()) {
                datas.add(this.tileContentVisitor.apply(content));
            }
        }
        return new TerritoryData(territory.getSize(), datas);
    }

    private LogEntry fromInitTerritory(final ObservableInitializeTerritoryCommandSpecification spec) {
        throw new UnsupportedOperationException("Hard resetting a recorded game is not (yet) supported.");
    }

    private LogEntry fromWrite(final ObservableWriteCommandSpecification writeCommandSpecification) {
        final LogEntry logEntry = new WriteLogEntry(this.hamsterIdLookup.get(writeCommandSpecification.getHamster()), writeCommandSpecification.getMessage());
        this.logEntries.add(logEntry);
        return logEntry;
    }

    private LogEntry fromInit(final ObservableInitHamsterCommandSpecification initCommandSpecification) {
        final HamsterData hamsterData = new HamsterData(
                initCommandSpecification.getLocation(),
                this.registerHamster(initCommandSpecification.getHamster()),
                initCommandSpecification.getNewDirection());
        final LogEntry logEntry = new InitHamsterLogEntry(hamsterData);

        this.logEntries.add(logEntry);
        return logEntry;
    }

    private LogEntry fromMove(final ObservableMoveCommandSpecification moveCommandSpecification) {
        final LogEntry logEntry = new MoveLogEntry(this.hamsterIdLookup.get(moveCommandSpecification.getHamster()));
        this.logEntries.add(logEntry);
        return logEntry;
    }

    private LogEntry fromTurnLeft(final ObservableTurnLeftCommandSpecification turnCommandSpecification) {
        final LogEntry logEntry = new TurnLeftLogEntry(this.hamsterIdLookup.get(turnCommandSpecification.getHamster()));
        this.logEntries.add(logEntry);
        return logEntry;
    }

    private LogEntry fromPutGrain(final ObservablePutGrainCommandSpecification putCommandSpecification) {
        final LogEntry logEntry = new PutGrainLogEntry(this.hamsterIdLookup.get(putCommandSpecification.getHamster()));
        this.logEntries.add(logEntry);
        return logEntry;
    }

    private LogEntry fromPickGrain(final ObservablePickGrainCommandSpecification pickCommandSpecification) {
        final LogEntry logEntry = new PickGrainLogEntry(this.hamsterIdLookup.get(pickCommandSpecification.getHamster()));
        this.logEntries.add(logEntry);
        return logEntry;
    }

    /**
     * Creates the GameLog created with this factory
     *
     * @return The created GameLog, contains all registered LogEntries
     */
    public GameLog toGameLog() {
        return new GameLog(this.territoryData, this.logEntries);
    }

    /**
     * Registers a new Hamster and assigns a unique id for it
     * This may only be called if this.lock is held
     *
     * @param hamster the Hamster to register
     * @return the assigned id
     */
    private int registerHamster(final ObservableHamster hamster) {
        final int newId = this.idCounter;
        this.idCounter++;
        this.hamsterIdLookup.put(hamster, newId);
        return newId;
    }

    /**
     * Takes a Command, and creates a LogEntry based on that command
     *
     * @param commandSpecification The CommandSpecification which should be transformed into a log entry
     * @return The created LogEntry
     */
    public LogEntry applyNextCommand(final ObservableCommandSpecification commandSpecification) {
        this.lock.lock();
        try {
            return this.logVisitor.apply(commandSpecification);
        } finally {
            this.lock.unlock();
        }
    }

}
