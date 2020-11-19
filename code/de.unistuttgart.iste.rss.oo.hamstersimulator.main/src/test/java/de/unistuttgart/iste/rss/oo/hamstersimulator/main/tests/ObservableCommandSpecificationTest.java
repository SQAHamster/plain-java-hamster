package de.unistuttgart.iste.rss.oo.hamstersimulator.main.tests;

import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.HamsterGameViewModel;
import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.InputInterface;
import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.ObservableLogEntry;
import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.command.specification.ObservableCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.command.specification.hamster.ObservableMoveCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.command.specification.hamster.ObservablePutGrainCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.command.specification.hamster.ObservableTurnLeftCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.adapter.observables.command.specification.hamster.ObservableWriteCommandSpecification;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Direction;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Size;
import de.unistuttgart.iste.rss.oo.hamstersimulator.external.model.Hamster;
import de.unistuttgart.iste.rss.oo.hamstersimulator.external.model.HamsterGame;
import de.unistuttgart.iste.rss.oo.hamstersimulator.external.model.TerritoryBuilder;
import javafx.beans.property.ReadOnlyListProperty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests which use the functionality of a SimpleHamsterGame by obtaining ObservableCommandSpecifications
 * via the ObservableLog
 */
public class ObservableCommandSpecificationTest {

    /**
     * HamsterGame used to execute tests, set with BeforeEach
     */
    private HamsterGame game;

    /**
     * Initializes game as 10x10 territory with the default hamster in the top left corner facing south and 10 grains
     * Also adds a mock InputInterface and starts the game
     */
    @BeforeEach
    public void initGame() {
        game = new HamsterGame();
        final TerritoryBuilder builder = game.getNewTerritoryBuilder();
        builder.initializeTerritory(new Size(10, 10));
        builder.defaultHamsterAt(Location.from(0, 0), Direction.SOUTH, 10);
        game.initialize(builder);
        final HamsterGameViewModel viewModel = game.getModelViewAdapter();
        viewModel.addInputInterface(getInputInterfaceMock());
        viewModel.getGameController().disableDelay();
        game.startGame();
    }

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

    /**
     * Executes the provided hamsterProgram with the default hamster and returns a list with all
     * ObservableCommandSpecification in the log
     *
     * @param hamsterProgram the program to execute, must be != null
     * @return a list with all ObservableCommandSpecifications from the log
     */
    private List<ObservableCommandSpecification> executeGame(final Consumer<Hamster> hamsterProgram) {
        game.runGame(territory -> hamsterProgram.accept(territory.getDefaultHamster()));
        final HamsterGameViewModel viewModel = game.getModelViewAdapter();
        final ReadOnlyListProperty<? extends ObservableLogEntry> log = viewModel.getLog().logProperty();
        return log.stream()
                .map(ObservableLogEntry::getCommandSpecification)
                .collect(Collectors.toList());
    }

    private <T extends ObservableCommandSpecification> List<ObservableCommandSpecification> filterCommandSpecifications(
            final List<ObservableCommandSpecification> specifications, Class<T> cls) {
        return specifications.stream()
                .filter(specification -> cls.isAssignableFrom(specification.getClass()))
                .collect(Collectors.toList());
    }

    /**
     * Gets a mock for the input interface
     * @return the mock
     */
    private InputInterface getInputInterfaceMock() {
        return Mockito.mock(InputInterface.class);
    }
}
