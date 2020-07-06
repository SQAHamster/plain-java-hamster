module rss.hamster.core {
    requires java.base;
    requires transitive rss.util;
    requires transitive javafx.base;

    exports de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes;
    exports de.unistuttgart.iste.rss.oo.hamstersimulator.exceptions;

    exports de.unistuttgart.iste.rss.oo.hamstersimulator.commands to rss.hamster.main, rss.hamster.ui;
    exports de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model to rss.hamster.main, rss.hamster.ui;
    exports de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.hamster to rss.hamster.main, rss.hamster.ui;
    exports de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.territory to rss.hamster.main, rss.hamster.ui;
    exports de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.hamster.command.specification
            to rss.hamster.main, rss.hamster.ui;

}