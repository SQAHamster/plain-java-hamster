package de.hamstersimulator.objectsfirst.internal.model;

import de.hamstersimulator.objectsfirst.adapter.observables.ObservableLog;
import de.hamstersimulator.objectsfirst.commands.Command;
import de.hamstersimulator.objectsfirst.commands.CommandSpecification;
import de.hamstersimulator.objectsfirst.datatypes.LogEntry;
import de.hamstersimulator.objectsfirst.internal.model.hamster.GameHamster;
import de.hamstersimulator.objectsfirst.internal.model.hamster.command.specification.*;
import de.hamstersimulator.objectsfirst.properties.ModifyPropertyCommand;
import de.hamstersimulator.objectsfirst.properties.ModifyPropertyCommandSpecification;
import de.hamstersimulator.objectsfirst.properties.ModifyPropertyCommandSpecification.ActionKind;
import de.hamstersimulator.objectsfirst.utils.LambdaVisitor;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Optional;
import java.util.function.Function;

public class GameLog implements ObservableLog {

    final ReadOnlyListWrapper<LogEntry> gameLog = new ReadOnlyListWrapper<LogEntry>(this, "log", FXCollections.observableArrayList());
    private final Function<CommandSpecification, Command> editCommandFactory;

    public GameLog() {
        super();
        this.editCommandFactory = new LambdaVisitor<CommandSpecification, Command>().
                on(MoveCommandSpecification.class).then(s -> this.getLogCommand(s, s.getHamster(), "Move")).
                on(TurnLeftCommandSpecification.class).then(s -> this.getLogCommand(s, s.getHamster(), "Turn Left")).
                on(InitHamsterCommandSpecification.class).then(s -> this.getLogCommand(s, s.getHamster(), "Init Hamster")).
                on(PickGrainCommandSpecification.class).then(s -> this.getLogCommand(s, s.getHamster(), "Pick Grain")).
                on(PutGrainCommandSpecification.class).then(s -> this.getLogCommand(s, s.getHamster(), "Put Grain")).
                on(WriteCommandSpecification.class).then(s -> this.getLogCommand(s, s.getHamster(), s.getMessage()));
    }

    /**
     * Getter for the log property, which contains read-only log entries
     *
     * @return the property (not null)
     */
    @Override
    public ReadOnlyListProperty<LogEntry> logProperty() {
        return this.gameLog.getReadOnlyProperty();
    }

    public Optional<Command> getCommandFromSpecification(final CommandSpecification specification) {
        return Optional.ofNullable(this.editCommandFactory.apply(specification));
    }

    private Command getLogCommand(final CommandSpecification specification, final GameHamster hamster, final String message) {
        return new ModifyPropertyCommand<ObservableList<LogEntry>>(this.gameLog, new ModifyPropertyCommandSpecification(new LogEntry(specification, hamster, message), ActionKind.ADD));
    }
}
