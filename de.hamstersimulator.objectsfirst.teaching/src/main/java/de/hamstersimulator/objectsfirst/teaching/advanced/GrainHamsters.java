package de.hamstersimulator.objectsfirst.teaching.advanced;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import de.hamstersimulator.objectsfirst.external.model.Hamster;

public class GrainHamsters {
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
