package de.unistuttgart.iste.rss.oo.examples.uml;

import java.util.HashSet;
import java.util.Set;

public class Student {

    private final String enrollmentNumber;
    private final Set<Lecture> attendedLectures = new HashSet<>();
    
    public Student(final String enrollmentNumber) {
        super();
        this.enrollmentNumber = enrollmentNumber;
    }
    
    public void register(){
        // TODO
    }

}
