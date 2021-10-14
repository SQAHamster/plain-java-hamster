package de.hamstersimulator.objectsfirst.internal.model.territory;

import de.hamstersimulator.objectsfirst.adapter.observables.ObservableWall;

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
