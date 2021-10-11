package de.hamstersimulator.objectsfirst.internal.model.territory;

import de.hamstersimulator.objectsfirst.adapter.observables.ObservableGrain;

public class Grain extends TileContent implements ObservableGrain {

    @Override
    public String toString() {
        return "Grain";
    }

    @Override
    protected boolean blocksEntrance() {
        return false;
    }
}
