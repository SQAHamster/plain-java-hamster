package de.unistuttgart.iste.rss.oo.hamstersimulator.examples;

import de.unistuttgart.iste.rss.oo.hamstersimulator.external.model.SimpleHamsterGame;

/**
 * Another hamster program. The idea is to create
 * Paula close to Paula with
 * grain in her mouth. She drops it and Paule picks it up.
 */
class Example04 extends SimpleHamsterGame {

    /**
     * Creates a new instance of Example04
     */
    public Example04() {
        game.initialize();
        game.startGame(false);
        displayInNewGameWindow();
    }

    @Override
    protected void run() {
        doubleMove();
    }

    /*@
      @ requires
      @   \forall int i; 1 <= i <= 2;
      @       \let Location l = paule.getDirection().getMovementVector().scale(i);
      @           game.getTerritory().isFree(\pre(paule.getLocation()).translate(l));
      @ ensures
      @    paule.getLocation().equals(\pre(paule.getLocation()).translate(paule.getDirection().getMovementVector().scale(2)));
      @*/
    void doubleMove() {
        // Precondition implies paule.frontIsClear();
        // From class invariant we also know
        // @ assert paule != null && paule.isInitalized
        paule.move();
        // assert paule.getLocation().equals(\pre(paule).getLocation().translate(paule.getDirection().getMovementVector().scale(1)))
        // Wegen der Invarianz des nicht spezifizierten Zustands gilt auch Precondition noch immer
        // (das Territory ändert sich nicht durch move()) ebenso, dass paule initialisiert ist
        // isFree(...) aus Precondition impliziert bei aktueller Position, dass paule.frontIsClear() gilt
        paule.move();
        // assert paule.getLocation().equals(\pre(paule).getLocation().translate(paule.getDirection().getMovementVector().scale(2)))
        // Q.E.D.
    }
}
