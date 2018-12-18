package de.unistuttgart.iste.rss.oo.examples.animals;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Dog extends Mamal {

    @Override
    public int pregnantFor() {
        return 60;
    }

    @Override
    public Set<Food> likes() {
        return new HashSet<Food>(Arrays.asList(Food.SAUSAGE));
    }

    @Override
    public String toString() {
        return "Dog [pregnantFor()=" + pregnantFor() + ", likes()=" + likes() + "]";
    }

}
