package de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.commands;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.EntityCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.PropertyMap;
import de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.Hamster;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.Territory;

abstract class HamsterCommand extends EntityCommand<Hamster> {

    protected final Hamster hamster;

    public HamsterCommand(final PropertyMap<Hamster> hamsterProperties) {
        super(hamsterProperties);
        this.hamster = hamsterProperties.getPropertyOwner();
    }

    protected Territory getTerritory() {
        assert this.entityState.getPropertyOwner().getCurrentTile().isPresent();

        return this.entityState.getPropertyOwner().getCurrentTerritory();
    }
}
