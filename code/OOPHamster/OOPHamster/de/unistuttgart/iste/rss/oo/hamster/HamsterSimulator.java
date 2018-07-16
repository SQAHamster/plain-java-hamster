package de.unistuttgart.iste.rss.oo.hamster;

import java.util.Collection;
import java.util.Collections;

public class HamsterSimulator {

    private Territory territory;

    private Collection<Hamster> otherHamsters;
    private final Hamster defaultHamster;
    private final CommandStack commandStack;

    public HamsterSimulator() {
        super();
        this.defaultHamster = new Hamster(this, 1, new Location(0,0));
        this.commandStack = new CommandStack(this);
    }

    public Collection<Hamster> getOtherHamsters() {
        return Collections.unmodifiableCollection(this.otherHamsters);
    }

    public Hamster getDefaultHamster() {
        return this.defaultHamster;
    }

    public Territory getTerritory() {
        return territory;
    }

    public CommandStack getCommandStack() {
        return this.commandStack;
    }

}