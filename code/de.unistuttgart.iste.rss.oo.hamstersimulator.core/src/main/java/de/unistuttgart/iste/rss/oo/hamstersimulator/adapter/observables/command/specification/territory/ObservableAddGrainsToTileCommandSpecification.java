package de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.command.specification.territory;

/**
 * observable version of command specification used to add amount grains to a specified tile
 */
public interface ObservableAddGrainsToTileCommandSpecification extends ObservableAbstractTerritoryTileCommandSpecification {

    /**
     * Get the amount of grains added to the tile
     * @return the amount of grains added to the tile, >= 0
     */
    int getAmount();

}
