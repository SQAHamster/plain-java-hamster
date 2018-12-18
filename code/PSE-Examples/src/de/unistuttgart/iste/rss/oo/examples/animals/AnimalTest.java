package de.unistuttgart.iste.rss.oo.examples.animals;

public class AnimalTest {

    void runTest() {
        final Hamster paule = new Hamster();
        final Animal pet1 = paule;
        final Mamal pet2 = paule;
    }
    
    void moreInterestingTest(final boolean wantDog) {
        final Hamster paule = new Hamster();
        final Dog waldi = new Dog();
        Animal pet;
        
        if (wantDog) {
            pet = waldi;
        } else {
            pet = paule;
        }
    }
}
