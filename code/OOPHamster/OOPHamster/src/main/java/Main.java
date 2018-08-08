import java.io.IOException;
import java.util.Optional;

import de.unistuttgart.iste.rss.oo.hamstersimulator.HamsterSimulator;
import de.unistuttgart.iste.rss.oo.hamstersimulator.commands.CommandStack;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Direction;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;
import de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.Hamster;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.Territory;
import de.unistuttgart.iste.rss.oo.hamstersimulator.territory.TerritoryLoader;
import de.unistuttgart.iste.rss.oo.hamstersimulator.ui.javafx.JavaFXUI;

public class Main {

    public static void main(final String[] args) throws IOException, CloneNotSupportedException {
        final String territoryFile = "/Users/snowball/test.ter";
        final HamsterSimulator simulator = new HamsterSimulator();

        JavaFXUI.start();
        JavaFXUI.getSingleton().init(simulator.getTerritory());


        //        class MyFlowableOnSubscribe implements FlowableOnSubscribe<HamsterStateChangedEvent> {
        //            @Override
        //            public void subscribe(final FlowableEmitter<HamsterStateChangedEvent> source) throws Exception {
        //                final HamsterStateListener listener = event -> source.onNext(event);
        //                paule.addHamsterStateListener(listener);
        //                l = listener;
        //            }
        //        }
        //        final MyFlowableOnSubscribe fos = new MyFlowableOnSubscribe();
        //
        //        final Flowable<HamsterStateChangedEvent> hamsterStateObservable =
        //                Flowable.create(fos, BackpressureStrategy.BUFFER).
        //                doOnCancel(()->System.out.println("Canceled")).
        //                doOnComplete(()->System.out.println("Completed")).
        //                doFinally(()->paule.removeHamsterStateListener(l));
        //
        //        final Disposable stateObserverDisposable = hamsterStateObservable.subscribe(System.out::println);

        final Territory territory = simulator.getTerritory();
        simulator.getCommandStack().execute(TerritoryLoader.loader(territory).loadFromFile(territoryFile));
        try {
            Thread.sleep(2000);
        } catch (final InterruptedException e) { }


        exampleRun(territory, simulator.getCommandStack());
        //        stateObserverDisposable.dispose();
        simulator.getCommandStack().undoAll();
    }

    private static void exampleRun(final Territory territory, final CommandStack commandStack) {
        final Hamster paule = territory.getDefaultHamster();
        final Hamster willi = new Hamster();
        commandStack.execute(willi.getInitializeHamsterCommand(Optional.of(territory), Optional.of(Location.from(1, 3)), Direction.WEST, 0));
        final Hamster marry = new Hamster();
        commandStack.execute(marry.getInitializeHamsterCommand(Optional.of(territory), Optional.of(Location.from(1, 2)), Direction.EAST, 0));

        while (!paule.grainAvailable() && paule.frontIsClear()) {
            paule.move(commandStack);
        }
        while (paule.grainAvailable()) {
            paule.pickGrain(commandStack);
        }
        paule.turnLeft(commandStack);
        paule.turnLeft(commandStack);
        while (paule.frontIsClear()) {
            paule.move(commandStack);
        }
        while (!paule.mouthEmpty()) {
            paule.putGrain(commandStack);
        }
        willi.move(commandStack);
        marry.move(commandStack);
    }
}
