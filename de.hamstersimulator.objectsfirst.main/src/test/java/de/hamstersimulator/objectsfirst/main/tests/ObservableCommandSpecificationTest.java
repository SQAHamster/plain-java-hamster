package de.hamstersimulator.objectsfirst.main.tests;

import de.hamstersimulator.objectsfirst.adapter.HamsterGameViewModel;
import de.hamstersimulator.objectsfirst.adapter.observables.ObservableLogEntry;
import de.hamstersimulator.objectsfirst.adapter.observables.command.specification.ObservableCommandSpecification;
import de.hamstersimulator.objectsfirst.adapter.observables.command.specification.hamster.ObservableMoveCommandSpecification;
import de.hamstersimulator.objectsfirst.adapter.observables.command.specification.hamster.ObservablePutGrainCommandSpecification;
import de.hamstersimulator.objectsfirst.adapter.observables.command.specification.hamster.ObservableTurnLeftCommandSpecification;
import de.hamstersimulator.objectsfirst.adapter.observables.command.specification.hamster.ObservableWriteCommandSpecification;
import de.hamstersimulator.objectsfirst.external.model.Hamster;
import javafx.beans.property.ReadOnlyListProperty;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests which use the functionality of a SimpleHamsterGame by obtaining ObservableCommandSpecifications
 * via the ObservableLog
 */
public class ObservableCommandSpecificationTest extends BaseHamsterGameTest {

    /**
     * Tests that the default hamster walks exactly 5 times
     */
    @Test
    public void testWalkFiveTimes() {
        final List<ObservableCommandSpecification> commandSpecifications = executeGame(paule -> {
            for (int i = 0; i < 5; i++) {
                paule.move();
            }
        });

        final List<ObservableCommandSpecification> moveSpecifications
                = filterCommandSpecifications(commandSpecifications, ObservableMoveCommandSpecification.class);
        assertEquals(5, moveSpecifications.size());
    }

    /**
     * Tests that the default hamster turns exactly 3 times
     */
    @Test
    public void testTurnThreeTimes() {
        final List<ObservableCommandSpecification> commandSpecifications = executeGame(paule -> {
            for (int i = 0; i < 3; i++) {
                paule.turnLeft();
            }
        });

        final List<ObservableCommandSpecification> turnSpecifications
                = filterCommandSpecifications(commandSpecifications, ObservableTurnLeftCommandSpecification.class);
        assertEquals(3, turnSpecifications.size());
    }

    /**
     * Tests that the default hamster writes the message "hello world"
     */
    @Test
    public void testWriteMessage() {
        final List<ObservableCommandSpecification> commandSpecifications = executeGame(paule -> {
            paule.write("hello world");
        });

        final List<ObservableCommandSpecification> writeSpecifications
                = filterCommandSpecifications(commandSpecifications, ObservableWriteCommandSpecification.class);
        assertEquals(1, writeSpecifications.size());
        final ObservableWriteCommandSpecification writeSpecification
                = (ObservableWriteCommandSpecification)writeSpecifications.get(0);
        assertEquals("hello world", writeSpecification.getMessage());
    }

    /**
     * Tests that the default hamster puts down 10 grains
     */
    @Test
    public void testPutDownTenGrains() {
        final List<ObservableCommandSpecification> commandSpecifications = executeGame(paule -> {
            for (int i = 0; i < 10; i++) {
                paule.putGrain();
            }
        });

        final List<ObservableCommandSpecification> putGrainSpecifications
                = filterCommandSpecifications(commandSpecifications, ObservablePutGrainCommandSpecification.class);
        assertEquals(10, putGrainSpecifications.size());
    }

    private <T extends ObservableCommandSpecification> List<ObservableCommandSpecification> filterCommandSpecifications(
            final List<ObservableCommandSpecification> specifications, Class<T> cls) {
        return specifications.stream()
                .filter(specification -> cls.isAssignableFrom(specification.getClass()))
                .collect(Collectors.toList());
    }

    /**
     * Executes the provided hamsterProgram with the default hamster and returns a list with all
     * ObservableCommandSpecification in the log
     *
     * @param hamsterProgram the program to execute, must be != null
     * @return a list with all ObservableCommandSpecifications from the log
     */
    protected List<ObservableCommandSpecification> executeGame(final Consumer<Hamster> hamsterProgram) {
        game.runGame(territory -> hamsterProgram.accept(territory.getDefaultHamster()));
        final HamsterGameViewModel viewModel = game.getModelViewAdapter();
        final ReadOnlyListProperty<? extends ObservableLogEntry> log = viewModel.getLog().logProperty();
        return log.stream()
                .map(ObservableLogEntry::getCommandSpecification)
                .collect(Collectors.toList());
    }
}
