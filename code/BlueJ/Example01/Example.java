
/**
 * Our first example program
 * 
 * @author Steffen Becker
 * @version 1.0
 */
public class Example extends SimpleHamsterGame
{
    void run() {
        game.initialize();
        game.displayInNewGameWindow();

        paule.move();
        paule.move();
        
        paule.pickGrain();
        paule.pickGrain();
        
        paule.move();
    }
}
