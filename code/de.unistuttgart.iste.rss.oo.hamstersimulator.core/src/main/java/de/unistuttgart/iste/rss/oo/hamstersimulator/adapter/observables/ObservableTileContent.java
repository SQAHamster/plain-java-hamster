package de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables;

import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.territory.Tile;
import javafx.beans.property.ReadOnlyObjectProperty;

import java.util.Optional;

/**
 * Interface to observe changes on a internal tile content
 * Provides read-only access to the tile on which this content currently is
 */
public interface ObservableTileContent {
    /**
     * Getter for the currentTile property
     * Provides access to the tile on which this content currently is.
     * A content must not always be on a tile, so this is optional
     * @return the property (not null)
     */
    ReadOnlyObjectProperty<Optional<Tile>> currentTileProperty();
}
