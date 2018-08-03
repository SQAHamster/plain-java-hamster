package de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.commands;

import java.util.Optional;

import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Direction;
import de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.Hamster.HamsterStateChanger;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.Grain;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.Tile;

public class InitHamsterCommand extends HamsterCommand {

    private Direction previousDirection;
    private final Direction newDirection;
    private final int newGrainCount;
    private Optional<Tile> previousTile;
    private int previousGrains;
    private final Optional<Tile> newTile;

    public InitHamsterCommand(final HamsterStateChanger hamsterStateAccess, final Optional<Tile> newTile, final Direction newDirection, final int newGrainCount) {
        super(hamsterStateAccess);
        this.newDirection = newDirection;
        this.newGrainCount = newGrainCount;
        this.newTile = newTile;
    }

    @Override
    public void execute() {
        assert this.hamster.getDirection() != null;

        this.previousDirection = this.hamster.getDirection();
        this.previousTile = this.hamster.getCurrentTile();
        this.previousGrains = this.hamster.getGrainInMouth().size();

        this.stateChanger.setDirection(newDirection);
        this.stateChanger.setCurrentTile(this.newTile);
        while(!this.hamster.getGrainInMouth().isEmpty()) {
            this.stateChanger.removeGrainFromMouth(this.stateChanger.getAnyGrain());
        }
        for (int i = 0; i < this.newGrainCount; i++) {
            this.stateChanger.addGrainToMouth(new Grain());
        }
        // TODO: this.stateListener.clear();
    }

    @Override
    public void undo() {
        this.stateChanger.setDirection(this.previousDirection);
        this.stateChanger.setCurrentTile(Optional.empty());
        while(!this.hamster.getGrainInMouth().isEmpty()) {
            this.stateChanger.removeGrainFromMouth(this.stateChanger.getAnyGrain());
        }
        for (int i = 0; i < this.previousGrains; i++) {
            this.stateChanger.addGrainToMouth(new Grain());
        }
    }

}
