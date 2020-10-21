package de.unistuttgart.iste.rss.oo.hamstersimulator.testFramework;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface HamsterTest {
    public String game() default "de.unistuttgart.iste.rss.oo.hamstersimulator.external.model.SimpleHamsterGame";
}
