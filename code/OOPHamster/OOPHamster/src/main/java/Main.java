import java.io.IOException;

import de.unistuttgart.iste.rss.oo.hamstersimulator.HamsterSimulator;
import de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.Hamster;
import de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.HamsterStateChangedEvent;
import de.unistuttgart.iste.rss.oo.hamstersimulator.hamster.HamsterStateListener;

public class Main {

    public static void main(final String[] args) throws IOException {
        final HamsterSimulator simulator = new HamsterSimulator("/Users/snowball/test.ter");
        final Hamster paule = simulator.getTerritory().getDefaultHamster();
        paule.addHamsterStateListener(new HamsterStateListener() {

            @Override
            public void onStateChanged(final HamsterStateChangedEvent e) {
                System.out.println(e.toString());
            }
        });

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
    }

}
