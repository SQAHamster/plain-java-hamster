package de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.hamster.command.specification;

import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.command.specification.hamster.ObservableWriteCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.hamster.GameHamster;

public class WriteCommandSpecification extends AbstractHamsterCommandSpecification implements ObservableWriteCommandSpecification {

    private final String message;

    public WriteCommandSpecification(final GameHamster hamster, final String message) {
        super(hamster);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

}
