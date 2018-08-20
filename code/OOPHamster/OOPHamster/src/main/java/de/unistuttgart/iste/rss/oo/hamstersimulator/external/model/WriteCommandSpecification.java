package de.unistuttgart.iste.rss.oo.hamstersimulator.external.model;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.specification.CommandSpecification;

public class WriteCommandSpecification implements CommandSpecification {

    private final String message;
    private final de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.hamster.ReadOnlyHamster sender;

    public de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.hamster.ReadOnlyHamster getSender() {
        return sender;
    }

    public WriteCommandSpecification(final String message, final de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.hamster.ReadOnlyHamster internalHamster) {
        super();
        this.message = message;
        this.sender = internalHamster;
    }

    public String getMessage() {
        return message;
    }

}
