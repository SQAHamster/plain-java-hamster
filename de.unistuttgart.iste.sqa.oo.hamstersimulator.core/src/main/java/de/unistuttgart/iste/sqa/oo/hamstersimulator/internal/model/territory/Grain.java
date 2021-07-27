package de.unistuttgart.iste.sqa.oo.hamstersimulator.internal.model.territory;

import de.unistuttgart.iste.sqa.oo.hamstersimulator.adapter.observables.ObservableGrain;

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
