package de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model;

import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.ObservableLog;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.Command;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.CommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.LogEntry;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.hamster.GameHamster;
import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.hamster.command.specification.*;
import de.unistuttgart.iste.rss.oo.hamstersimulator.properties.ModifyPropertyCommand;
import de.unistuttgart.iste.rss.oo.hamstersimulator.properties.ModifyPropertyCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.properties.ModifyPropertyCommandSpecification.ActionKind;
import de.unistuttgart.iste.rss.utils.LambdaVisitor;
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
