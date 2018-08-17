package de.unistuttgart.iste.rss.oo.hamstersimulator.external.model;

import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.GameTerritory;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.TerritoryLoader;

public class Territory {

    private final Hamster defaultHamster;
    private final HamsterGame game;
    private final GameTerritory internalTerritory;

    public Territory(final HamsterGame game) {
        super();
        this.game = game;
        this.internalTerritory = new GameTerritory();
        this.defaultHamster = Hamster.fromInternalDefaultHamster(this);
    }

    public Hamster getDefaultHamster() {
        return this.defaultHamster;
    }

    public HamsterGame getGame() {
        return this.game;
    }

    public void loadFromFile(final String territoryFile) {
        TerritoryLoader.initializeFor(this.game.getInternalTerritory()).loadFromFile(territoryFile);
    }

    protected static void delay(final int delay) {
        try {
            Thread.sleep(delay);
        } catch (final InterruptedException e) { }
    }

    public GameTerritory getInternalTerritory() {
        return this.internalTerritory;
    }
}
