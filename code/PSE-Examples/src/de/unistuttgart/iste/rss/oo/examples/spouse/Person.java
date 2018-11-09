package de.unistuttgart.iste.rss.oo.examples.spouse;

import java.util.Optional;

/**
 * Class to model potentially
 * married persons.
 * @author Steffen Becker
 */
class Person {

    /*
     * This person's spouse if person is married
     */
    Optional<Person> spouse;
    
    /*
     * Create a new person and link it
     * optionally to its spouse
     */
    Person() {
        this.spouse = Optional.empty();
    }

    /*
     * Returns this person's spouse
     * @throws IllegalStateException if the person
     * is not married and has no spouse.
     */
    Person getSpouse() {
        return this.spouse.
               orElseThrow(IllegalStateException::new);
    }
    
    void marry(final Person spouse) {
        this.spouse = Optional.of(spouse);
        spouse.spouse = Optional.of(this);
    }
}

class TestSpouse {
    
    void runTest() {
        Person unmarriedPerson = new Person();
        Person paul = new Person();
        Person clara = new Person();
        paul.marry(clara);
    }
    
}