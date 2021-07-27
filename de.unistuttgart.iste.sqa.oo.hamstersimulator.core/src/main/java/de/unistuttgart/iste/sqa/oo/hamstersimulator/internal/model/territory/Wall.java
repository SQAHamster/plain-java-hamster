package de.unistuttgart.iste.sqa.oo.hamstersimulator.internal.model.territory;

import de.unistuttgart.iste.sqa.oo.hamstersimulator.adapter.observables.ObservableWall;

public class Wall extends TileContent implements ObservableWall {

    @Override
    public String toString() {
        return "Wall";
    }

    @Override
    protected boolean blocksEntrance() {
        return true;
    }

}
