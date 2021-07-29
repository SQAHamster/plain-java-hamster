package de.unistuttgart.iste.sqa.oo.hamstersimulator.testframework.gamelog.datatypes;

import de.unistuttgart.iste.sqa.oo.hamstersimulator.datatypes.Size;

import java.util.List;

public class TerritoryData {
    private final Size size;
    private final List<TileContentData> tileContents;

    public TerritoryData(final Size size, final List<TileContentData> tileContents) {
        this.size = size;
        this.tileContents = tileContents;
    }
}
