package de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.hamster.command.specification;

import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.hamster.GameHamster;

abstract class AbstractHamsterCommandSpecification {
    private final GameHamster hamster;
    
    public AbstractHamsterCommandSpecification(final GameHamster hamster) {
        super();
        this.hamster = hamster;
    }
    
    public GameHamster getHamster() {
        return hamster;
    }

}
