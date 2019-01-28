package de.unistuttgart.iste.rss.oo.examples.uml;

import java.util.HashSet;
import java.util.Set;

public class Lecture {
    private final String lectureRoom;
    private final Set<Student> students;
    
    public Lecture(final String lectureRoom, 
            final Set<Student> students) {
        super();
        this.lectureRoom = lectureRoom;
        this.students = new HashSet<Student>(students);
    }
    
    public void schedule() {
        // TODO
    }
    
    public void cancel() {
        // TODO
    }
}
