package de.unistuttgart.iste.rss.oo.hamstersimulator.commands.specification;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.AbstractCompositeCommand;

public class AbstractSpecifiedCompositeCommand extends AbstractCompositeCommand implements SpecifiedCommand<CompositeCommandSpecification> {

    @Override
    public CompositeCommandSpecification getSpecification() {
        return new CompositeCommandSpecification(this);
    }

}
