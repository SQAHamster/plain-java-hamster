module de.hamstersimulator.objectsfirst.core {
    requires transitive de.hamstersimulator.objectsfirst.util;
    requires transitive javafx.base;

    exports de.hamstersimulator.objectsfirst.datatypes;
    exports de.hamstersimulator.objectsfirst.exceptions;
    exports de.hamstersimulator.objectsfirst.adapter;
    exports de.hamstersimulator.objectsfirst.adapter.observables;
    exports de.hamstersimulator.objectsfirst.adapter.observables.command.specification;
    exports de.hamstersimulator.objectsfirst.adapter.observables.command.specification.hamster;
    exports de.hamstersimulator.objectsfirst.adapter.observables.command.specification.territory;

    exports de.hamstersimulator.objectsfirst.commands to de.hamstersimulator.objectsfirst.main;
    exports de.hamstersimulator.objectsfirst.internal.model to de.hamstersimulator.objectsfirst.main;
    exports de.hamstersimulator.objectsfirst.internal.model.hamster to de.hamstersimulator.objectsfirst.main;
    exports de.hamstersimulator.objectsfirst.internal.model.territory to de.hamstersimulator.objectsfirst.main;
    exports de.hamstersimulator.objectsfirst.internal.model.hamster.command.specification
            to de.hamstersimulator.objectsfirst.main;

    opens de.hamstersimulator.objectsfirst.datatypes to com.google.gson;

}
