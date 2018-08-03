package de.unistuttgart.iste.rss.oo.hamstersimulator.territory.tile;

public class Grain extends TileContent {

    @Override
    public String toString() {
        return "Grain";
    }

    public Grain() {
        super();
    }

    @Override
    protected boolean blocksEntrance() {
        return false;
    }
}