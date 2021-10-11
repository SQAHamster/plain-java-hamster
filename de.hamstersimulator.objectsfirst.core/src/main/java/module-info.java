module sqa.hamster.core {
    requires transitive sqa.util;
    requires transitive javafx.base;

    exports de.hamstersimulator.objectsfirst.datatypes;
    exports de.hamstersimulator.objectsfirst.exceptions;
    exports de.hamstersimulator.objectsfirst.adapter;
    exports de.hamstersimulator.objectsfirst.adapter.observables;
    exports de.hamstersimulator.objectsfirst.adapter.observables.command.specification;
    exports de.hamstersimulator.objectsfirst.adapter.observables.command.specification.hamster;
    exports de.hamstersimulator.objectsfirst.adapter.observables.command.specification.territory;

    exports de.hamstersimulator.objectsfirst.commands to sqa.hamster.main;
    exports de.hamstersimulator.objectsfirst.internal.model to sqa.hamster.main;
    exports de.hamstersimulator.objectsfirst.internal.model.hamster to sqa.hamster.main;
    exports de.hamstersimulator.objectsfirst.internal.model.territory to sqa.hamster.main;
    exports de.hamstersimulator.objectsfirst.internal.model.hamster.command.specification
            to sqa.hamster.main;

    opens de.hamstersimulator.objectsfirst.datatypes to com.google.gson;

}
