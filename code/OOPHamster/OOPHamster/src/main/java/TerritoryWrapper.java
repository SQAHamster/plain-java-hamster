import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.Territory;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.TerritoryLoader;

public class TerritoryWrapper {
    private final Territory territory;
    private final String territoryFile;

    public TerritoryWrapper(final Territory Territory, final String territoryFile) {
        super();
        this.territory = Territory;
        this.territoryFile = territoryFile;
    }

    void initialize() {
        TerritoryLoader.initializeFor(territory).loadFromFile(territoryFile);
    }

    public Territory getTerritory() {
        return this.territory;
    }
}
