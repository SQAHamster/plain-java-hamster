package de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.AbstractCompositeCommand;

abstract class AbstractTerritoryCompositeBaseCommand extends AbstractCompositeCommand {

    protected final Territory territory;

    public AbstractTerritoryCompositeBaseCommand(final Territory territory) {
        super();
        this.territory = territory;
    }

}
