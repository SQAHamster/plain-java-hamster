import de.unistuttgart.iste.rss.oo.hamstersimulator.ui.javafx.JavaFXUI;

public class GamefieldWrapper {

    private final TerritoryWrapper territory;

    public GamefieldWrapper(final TerritoryWrapper territory) {
        super();
        this.territory = territory;
    }

    public void show() {
        JavaFXUI.getSingleton().init(territory.getTerritory());
    }

}
