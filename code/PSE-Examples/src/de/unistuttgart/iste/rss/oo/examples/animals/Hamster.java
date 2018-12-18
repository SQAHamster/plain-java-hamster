package de.unistuttgart.iste.rss.oo.examples.animals;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Hamster extends Mamal {

    @Override
    public int pregnantFor() {
        return 30;
    }

    @Override
    public Set<Food> likes() {
        return new HashSet<Food>(Arrays.asList(Food.GRAIN));
    }

    @Override
    public String toString() {
        return "Hamster [pregnantFor()=" + pregnantFor() + ", likes()=" + likes() + "]";
    }

    
}