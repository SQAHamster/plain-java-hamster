package de.unistuttgart.iste.sqa.oo.hamstersimulator.inspector.testdata;

import java.util.ArrayList;
import java.util.List;

public class B implements A {
    public int x = 0;

    public String test = "Hello world";

    public String strTest2 = null;

    public Object testObj = null;

    public B b = null;

    public List<String> list = new ArrayList<>();

    public String testing() {
        return "Hello world";
    }

    public void writeString(final String value, int testing) {
        System.out.println(value);
    }
}
