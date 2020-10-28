package de.unistuttgart.iste.rss.oo.hamstersimulator.template;

import de.unistuttgart.iste.rss.oo.hamstersimulator.external.model.SimpleHamsterGame;

class MyHamsterProgram extends SimpleHamsterGame {

    @Override
    protected void run() {
        game.initialize();
        this.displayInNewGameWindow();

        game.startGame();
        paule.move();
    }

}
