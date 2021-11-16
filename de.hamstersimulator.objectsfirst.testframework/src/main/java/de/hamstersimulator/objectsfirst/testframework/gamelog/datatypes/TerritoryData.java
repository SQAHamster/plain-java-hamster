package de.hamstersimulator.objectsfirst.testframework.gamelog.datatypes;

import de.hamstersimulator.objectsfirst.datatypes.Size;

import java.util.List;

/**
 * Represents the state of a Territory
 *
 * @param size the size of the Territory
 * @param tileContents a list of datas representing all TileContents
 */
public record TerritoryData(Size size, List<TileContentData> tileContents) { }
