package de.unistuttgart.iste.rss.oo.hamstersimulator.commands.specification;

public interface SpecifiedCommand<T extends CommandSpecification> {
    public T getSpecification();

    @SuppressWarnings("unchecked")
    default public T cloneSpecification() throws CloneNotSupportedException { return (T) getSpecification().clone(); }
}
