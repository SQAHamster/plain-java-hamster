package de.hamstersimulator.objectsfirst.inspector.tests;

import de.hamstersimulator.objectsfirst.external.simple.game.SimpleHamsterGame;
import de.hamstersimulator.objectsfirst.inspector.InspectableSimpleHamsterGame;

public class TestInspectorGame extends InspectableSimpleHamsterGame {
    @Override
    protected void run() {
        game.startGame();
        paule.move();
    }

    public static void main(String[] args) {
        TestInspectorGame game = new TestInspectorGame();
        game.loadTerritoryFromResourceFile("/example01.ter");
        game.displayInNewGameWindow();
        game.doRun();
    }
}
