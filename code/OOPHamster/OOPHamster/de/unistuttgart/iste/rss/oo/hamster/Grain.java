package de.unistuttgart.iste.rss.oo.hamster;

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