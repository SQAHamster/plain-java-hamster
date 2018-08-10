package de.unistuttgart.iste.rss.oo.hamstersimulator.commands;

public class AbstractSpecifiedCompositeCommand extends AbstractCompositeCommand implements SpecifiedCommand<CompositeCommandSpecification> {

    @Override
    public CompositeCommandSpecification getSpecification() {
        return new CompositeCommandSpecification(this);
    }

}
