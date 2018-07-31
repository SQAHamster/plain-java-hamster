package de.unistuttgart.iste.rss.oo.hamstersimulator.hamster;

import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Direction;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.Grain;

public interface HamsterStateChanger {

    void setDirection(Direction direction);

    void addGrainToMouth(Grain newGrain);

    void removeGrainFromMouth(Grain grainToRemove);

    Hamster getHamster();

    Grain getAnyGrain();

}