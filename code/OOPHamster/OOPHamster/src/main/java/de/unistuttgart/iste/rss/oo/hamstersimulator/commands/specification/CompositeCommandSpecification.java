package de.unistuttgart.iste.rss.oo.hamstersimulator.commands.specification;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class CompositeCommandSpecification implements CommandSpecification {

    private final List<CommandSpecification> childSpecification = new LinkedList<>();

    public CompositeCommandSpecification(final CommandSpecification ... commandSpecifications) {
        super();
        for (final CommandSpecification commandSpec : commandSpecifications) {
            childSpecification.add(commandSpec);
        }
    }

    public List<CommandSpecification> getChildSpecification() {
        return Collections.unmodifiableList(childSpecification);
    }

    @Override
    public void visit(final SpecificationVisitor visitor) {
        childSpecification.forEach(spec -> spec.visit(visitor));
    }

}
