package de.unistuttgart.iste.sqa.oo.hamstersimulator.testframework.gamelog;

import de.unistuttgart.iste.sqa.oo.hamstersimulator.adapter.observables.*;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.adapter.observables.command.specification.ObservableCommandSpecification;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.adapter.observables.command.specification.hamster.*;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.adapter.observables.command.specification.territory.ObservableInitializeTerritoryCommandSpecification;
import de.unistuttgart.iste.sqa.oo.hamstersimulator.testframework.gamelog.datatypes.*;
import de.unistuttgart.iste.sqa.utils.LambdaVisitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public final class GameLogFactory {

    /**
     * Dispatcher used to execute state change logic dependant on the command
     * specification executed on the previous state. The return value is not used here.
     */
    private final LambdaVisitor<ObservableCommandSpecification, LogEntry> logVisitor;

    private final LambdaVisitor<ObservableTileContent, TileContentData> tileContentVisitor;

    private final TerritoryData territoryData;

    private final List<LogEntry> logEntries = new ArrayList<>();

    private final Map<ObservableHamster, Integer> hamsterIdLookup = new HashMap<>();

    /**
     * counter for the TileContent id
     */
    private volatile int idCounter = 0;

    /**
     * Lock used for synchronization
     */
    private final ReentrantLock lock = new ReentrantLock(true);

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
                .on(ObservableHamster.class).then(hamster -> new HamsterData(hamster.getCurrentLocation().orElseThrow(), this.hamsterIdLookup.get(hamster), hamster.getDirection()))
                .on(ObservableGrain.class).then(grain -> new GrainData(grain.getCurrentLocation().orElseThrow()))
                .on(ObservableWall.class).then(wall -> new WallData(wall.getCurrentLocation().orElseThrow()));

        this.territoryData = this.createTerritoryData(territory);
    }

    private TerritoryData createTerritoryData(final ObservableTerritory territory) {
        for (final ObservableHamster hamster : territory.getHamsters()) {
            this.registerHamster(hamster);
        }
        final List<TileContentData> datas = new ArrayList<>();
        for (final ObservableTile tile : territory.tilesProperty()) {
            for (final ObservableTileContent content : tile.contentProperty()) {
                System.out.println(content.getClass());
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
        final LogEntry logEntry = new InitHamsterLogEntry(
                new HamsterData(
                        initCommandSpecification.getLocation(),
                        this.registerHamster(initCommandSpecification.getHamster()
                        ), initCommandSpecification.getNewDirection()));
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

    public GameLog toGameLog() {
        return new GameLog(this.territoryData, this.logEntries);
    }

    private int registerHamster(final ObservableHamster hamster) {
        final int newId = this.idCounter;
        this.idCounter++;
        this.hamsterIdLookup.put(hamster, newId);
        return newId;
    }

    public LogEntry applyNextCommand(final ObservableCommandSpecification commandSpecification) {
        this.lock.lock();
        try {
            return this.logVisitor.apply(commandSpecification);
        } finally {
            this.lock.unlock();
        }
    }

}
