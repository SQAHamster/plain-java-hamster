package de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.command.specification;

import java.util.Optional;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.CommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.SpecificationVisitor;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Direction;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;

public final class InitHamsterCommandSpecification implements CommandSpecification {

    private final Optional<Location> location;
    private final Direction newDirection;
    private final int newGrainCount;

    public InitHamsterCommandSpecification(final Optional<Location> location, final Direction newDirection, final int newGrainCount) {
        this.location = location;
        this.newDirection = newDirection;
        this.newGrainCount = newGrainCount;
    }

    public Optional<Location> getLocation() {
        return location;
    }

    public Direction getNewDirection() {
        return newDirection;
    }

    public int getNewGrainCount() {
        return newGrainCount;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public void visit(final SpecificationVisitor visitor) {
        visitor.visit(this);
    }

}