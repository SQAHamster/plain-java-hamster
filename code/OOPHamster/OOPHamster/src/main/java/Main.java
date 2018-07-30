import java.io.IOException;

import de.unistuttgart.iste.rss.oo.hamstersimulator.HamsterSimulator;
import de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.Hamster;
import de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.HamsterStateChangedEvent;
import de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.HamsterStateListener;
import de.unistuttgart.iste.rss.oo.hamstersimulator.ui.javafx.JavaFXUI;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.disposables.Disposable;

public class Main {

    public static HamsterStateListener l;

    public static void main(final String[] args) throws IOException {
        final HamsterSimulator simulator = new HamsterSimulator("/Users/snowball/test.ter");
        final Hamster paule = simulator.getTerritory().getDefaultHamster();

        class MyFlowableOnSubscribe implements FlowableOnSubscribe<HamsterStateChangedEvent> {
            @Override
            public void subscribe(final FlowableEmitter<HamsterStateChangedEvent> source) throws Exception {
                final HamsterStateListener listener = event -> source.onNext(event);
                paule.addHamsterStateListener(listener);
                l = listener;
            }
        }
        final MyFlowableOnSubscribe fos = new MyFlowableOnSubscribe();

        final Flowable<HamsterStateChangedEvent> hamsterStateObservable =
                Flowable.create(fos, BackpressureStrategy.BUFFER).
                doOnCancel(()->System.out.println("Canceled")).
                doOnComplete(()->System.out.println("Completed")).
                doFinally(()->paule.removeHamsterStateListener(l));

        final Disposable stateObserverDisposable = hamsterStateObservable.subscribe(System.out::println);

        JavaFXUI.start();
        JavaFXUI.getSingleton().init(simulator.getTerritory());
        try {
            Thread.sleep(2000);
        } catch (final InterruptedException e) { }

        while (!paule.grainAvailable() && paule.frontIsClear()) {
            paule.move();
        }
        while (paule.grainAvailable()) {
            paule.pickGrain();
        }
        paule.turnLeft();
        paule.turnLeft();
        while (paule.frontIsClear()) {
            paule.move();
        }
        while (!paule.mouthEmpty()) {
            paule.putGrain();
        }
        stateObserverDisposable.dispose();
    }
}
