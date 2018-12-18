package de.unistuttgart.iste.rss.oo.examples.animals;

public class OverrideTest {

    public static void main(final String[] args) {
        final Hamster paule = new Hamster();
        final Dog waldi = new Dog();
        
        System.out.println(paule.toString());
        System.out.println(waldi.toString());
    }
    
}
