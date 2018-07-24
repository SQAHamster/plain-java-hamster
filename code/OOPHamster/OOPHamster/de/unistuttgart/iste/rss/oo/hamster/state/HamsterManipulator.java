package de.unistuttgart.iste.rss.oo.hamster.state;

import de.unistuttgart.iste.rss.oo.hamster.Grain;
import de.unistuttgart.iste.rss.oo.hamster.Hamster;
import de.unistuttgart.iste.rss.oo.hamster.Territory;
import de.unistuttgart.iste.rss.oo.hamster.datatypes.Direction;
import de.unistuttgart.iste.rss.oo.hamster.datatypes.Location;

public class HamsterManipulator {

    private final HamsterState state;
    private final Hamster hamster;
    private final Territory territory;

    public HamsterManipulator(final Hamster hamster, final HamsterState state, final Territory territory) {
        super();
        this.state = state;
        this.hamster = hamster;
        this.territory = territory;
    }

    public Hamster getHamster() {
        return hamster;
    }

    public Territory getTerritory() {
        return this.territory;
    }

    public void setLocation(final Location newLocation) {
        this.state.setCurrentTile(this.territory.getTileAt(newLocation));
    }

    public void setDirection(final Direction newDirection) {
        this.state.setDirection(newDirection);
    }

    public void addGrain(final Grain pickedGrain) {
        this.state.addGrainToMouth(pickedGrain);
    }

    public Grain removeAnyGrain() {
        final Grain result = this.state.getGrainInMouth().get(0);
        this.state.removeGrainFromMouth(result);
        return result;
    }
}
