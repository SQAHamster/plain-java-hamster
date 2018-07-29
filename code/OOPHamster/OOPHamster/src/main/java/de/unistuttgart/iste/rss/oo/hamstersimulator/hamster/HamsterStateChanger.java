package de.unistuttgart.iste.rss.oo.hamstersimulator.hamster;

import java.util.Optional;

import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Direction;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.Grain;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.Tile;

public interface HamsterStateChanger {

    void setCurrentTile(Optional<Tile> newTile);

    void setDirection(Direction direction);

    void addGrainToMouth(Grain newGrain);

    void removeGrainFromMouth(Grain grainToRemove);

    Hamster getHamster();

    Grain getAnyGrain();

}