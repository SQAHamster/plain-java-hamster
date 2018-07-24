import de.unistuttgart.iste.rss.oo.hamster.Hamster;
import de.unistuttgart.iste.rss.oo.hamster.HamsterSimulator;
import de.unistuttgart.iste.rss.oo.hamster.state.HamsterStateChangedEvent;
import de.unistuttgart.iste.rss.oo.hamster.state.HamsterStateListener;

public class Main {

    public static void main(final String[] args) {
        final HamsterSimulator simulator = new HamsterSimulator("/Users/snowball/test.ter");
        final Hamster paule = simulator.getTerritory().getDefaultHamster();
        paule.addHamsterStateListener(new HamsterStateListener() {

            @Override
            public void onStateChanged(final HamsterStateChangedEvent e) {
                System.out.println(e.toString());
            }
        });

        paule.move();
        paule.move();
        paule.pickGrain();
    }

}
