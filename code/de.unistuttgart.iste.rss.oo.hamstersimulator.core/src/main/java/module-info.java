module rss.hamster.core {
    requires transitive rss.util;
    requires transitive javafx.base;

    exports de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes;
    exports de.unistuttgart.iste.rss.oo.hamstersimulator.exceptions;
    exports de.unistuttgart.iste.rss.oo.hamstersimulator.adapter;
    exports de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables;
    exports de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.command.specification;
    exports de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.command.specification.hamster;
    exports de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.command.specification.territory;

    exports de.unistuttgart.iste.rss.oo.hamstersimulator.commands to rss.hamster.main;
    exports de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model to rss.hamster.main;
    exports de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.hamster to rss.hamster.main;
    exports de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.territory to rss.hamster.main;
    exports de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.hamster.command.specification
            to rss.hamster.main;

    opens de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes to com.google.gson;

}