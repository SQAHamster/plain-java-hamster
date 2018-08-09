
class Example01 extends SimpleHamsterGame {

    @Override
    void run() {
        showGamefield();
        initializeTerritory();

        paule.move();
        paule.move();
        paule.pickGrain();
        paule.pickGrain();
        paule.turnLeft();
        paule.turnLeft();
        paule.move();
        paule.move();
        paule.turnLeft();
        paule.turnLeft();
    }

}
