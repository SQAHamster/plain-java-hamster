package de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.command.specification.territory;

import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.command.specification.ObservableCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Size;

public interface ObservableInitializeTerritoryCommandSpecification extends ObservableCommandSpecification {

    Size getSize();
}
