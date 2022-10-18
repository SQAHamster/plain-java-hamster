package de.hamstersimulator.objectsfirst.teaching.lecture08;

import de.hamstersimulator.objectsfirst.teaching.base.SimpleHamsterGameBase;

class Example03 extends SimpleHamsterGame {

    /**
     * Another hamster program. It demonstrates a double move.
     */
    void run() {
        game.initialize();
        game.displayInNewGameWindow();

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
