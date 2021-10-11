module sqa.hamster.core {
    requires transitive sqa.util;
    requires transitive javafx.base;

    exports de.unistuttgart.iste.sqa.oo.hamstersimulator.datatypes;
    exports de.unistuttgart.iste.sqa.oo.hamstersimulator.exceptions;
    exports de.unistuttgart.iste.sqa.oo.hamstersimulator.adapter;
    exports de.unistuttgart.iste.sqa.oo.hamstersimulator.adapter.observables;
    exports de.unistuttgart.iste.sqa.oo.hamstersimulator.adapter.observables.command.specification;
    exports de.unistuttgart.iste.sqa.oo.hamstersimulator.adapter.observables.command.specification.hamster;
    exports de.unistuttgart.iste.sqa.oo.hamstersimulator.adapter.observables.command.specification.territory;

    exports de.unistuttgart.iste.sqa.oo.hamstersimulator.commands to sqa.hamster.main;
    exports de.unistuttgart.iste.sqa.oo.hamstersimulator.internal.model to sqa.hamster.main;
    exports de.unistuttgart.iste.sqa.oo.hamstersimulator.internal.model.hamster to sqa.hamster.main;
    exports de.unistuttgart.iste.sqa.oo.hamstersimulator.internal.model.territory to sqa.hamster.main;
    exports de.unistuttgart.iste.sqa.oo.hamstersimulator.internal.model.hamster.command.specification
            to sqa.hamster.main;

    opens de.unistuttgart.iste.sqa.oo.hamstersimulator.datatypes to com.google.gson;

}
