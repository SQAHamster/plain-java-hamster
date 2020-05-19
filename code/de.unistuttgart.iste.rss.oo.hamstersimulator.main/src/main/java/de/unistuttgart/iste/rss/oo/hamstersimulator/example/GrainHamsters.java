package de.unistuttgart.iste.rss.oo.hamstersimulator.example;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import de.unistuttgart.iste.rss.oo.hamstersimulator.external.model.Hamster;

public class GrainHamsters {

    public static void main(String[] args) {
        

    }
    
    /**
     * ...
     * @param hamsters
     * @return
     */
    public Set<Hamster> getHamstersWithGrain(final Set<Hamster> hamsters) {
        final Set<Hamster> hamstersWithGrain = new HashSet<>();
        for (final Hamster hamster : hamsters) {
            if (!hamster.mouthEmpty()) {
                hamstersWithGrain.add(hamster);
            }
        }
        return Collections.unmodifiableSet(hamstersWithGrain);
    }
}
