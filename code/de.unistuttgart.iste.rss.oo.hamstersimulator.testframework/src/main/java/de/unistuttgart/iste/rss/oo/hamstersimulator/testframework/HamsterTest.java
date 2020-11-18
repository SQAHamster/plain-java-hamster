package de.unistuttgart.iste.rss.oo.hamstersimulator.testframework;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface HamsterTest {
    /**
     * Used to provide the HamsterGameResolver the fully qualified class name of a SimpleHamsterGame
     * which can be used to provide the TestUtils parameter
     * @return the value provided to the annotation, default the SimpleHamsterGame's fully qualified
     *         class name
     */
    String game() default "de.unistuttgart.iste.rss.oo.hamstersimulator.external.model.SimpleHamsterGame";
}
