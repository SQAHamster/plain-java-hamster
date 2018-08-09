package de.unistuttgart.iste.rss.oo.hamstersimulator.commands;

import static com.google.common.base.Preconditions.checkNotNull;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.specification.CommandSpecification;

public abstract class AbstractBaseCommand<T extends CommandSpecification> implements CommandInterface<T> {

    private final T spec;

    public AbstractBaseCommand(final T spec) {
        super();
        checkNotNull(spec);
        this.spec = spec;
    }

    @Override
    public T getSpecification() {
        return spec;
    }

    protected abstract void execute();
    protected abstract void undo();

}
