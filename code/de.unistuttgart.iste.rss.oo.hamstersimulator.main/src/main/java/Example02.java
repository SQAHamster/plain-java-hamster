import de.unistuttgart.iste.rss.oo.hamstersimulator.external.model.Hamster;

class Example02 extends SimpleHamsterGame {
    Hamster paula;

    /**
     * Another hamster program. The idea is to create Paula close to Paula with
     * grain in her mouth. She drops it and Paule picks it up.     
     */
    @Override
    void run() {
        game.initialize();
        game.displayInNewGameWindow();

        Hamster paula = new Hamster(game.getTerritory(), paule.getLocation(), paule.getDirection(), 0);

        paula.putGrain();
        paule.move();
        paule.pickGrain();
    }
}