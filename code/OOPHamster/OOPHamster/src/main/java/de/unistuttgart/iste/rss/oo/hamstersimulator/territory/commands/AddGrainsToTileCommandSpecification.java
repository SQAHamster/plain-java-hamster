package de.unistuttgart.iste.rss.oo.hamstersimulator.territory.commands;

import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.specification.CommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.specification.SpecificationVisitor;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;

public class AddGrainsToTileCommandSpecification implements CommandSpecification {

    private final Location location;
    private final int amount;

    public AddGrainsToTileCommandSpecification(final Location location, final int amount) {
        super();
        this.location = location;
        this.amount = amount;
    }

    public Location getLocation() {
        return location;
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public void visit(final SpecificationVisitor visitor) {
        visitor.visit(this);
    }

}
