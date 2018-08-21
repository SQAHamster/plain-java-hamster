package de.unistuttgart.iste.rss.oo.hamstersimulator.external.model;

import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.territory.GameTerritory;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.territory.TerritoryLoader;

public class Territory {

    private final HamsterGame game;
    private final GameTerritory internalTerritory;
    private final Hamster defaultHamster;

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
        TerritoryLoader.initializeFor(this.internalTerritory).loadFromFile(territoryFile);
    }

    GameTerritory getInternalTerritory() {
        return this.internalTerritory;
    }

}
