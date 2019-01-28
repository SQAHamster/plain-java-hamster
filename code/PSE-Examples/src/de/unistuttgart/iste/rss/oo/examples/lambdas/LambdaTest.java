package de.unistuttgart.iste.rss.oo.examples.lambdas;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class LambdaTest {
    private final static List<String> words = 
            Arrays.asList("Hallo", "Welt", "ich", "bin", "da");

    public static List<String> captialWords() {
        final List<String> result = new LinkedList<String>();
        for (final String word : words) {
            if (Character.isUpperCase(word.charAt(0))) {
                result.add(word);
            }
        }
        return result;
    }
    public static List<String> lowercaseWords() {
        final List<String> result = new LinkedList<String>();
        for (final String word : words) {
            if (Character.isLowerCase(word.charAt(0))) {
                result.add(word);
            }
        }
        return result;
    }
}
