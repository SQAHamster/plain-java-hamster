package de.unistuttgart.iste.rss.oo.examples.lambdas;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@FunctionalInterface
interface Predicate<G> {
    boolean isMatch(G value);
}

public class LambdaTest2 {
    private final static List<String> words = 
            Arrays.asList("Hallo", "Welt", "ich", "bin", "da");
    private final static Predicate<String> upperCase = (s) -> Character.isUpperCase(s.charAt(0));
    private final static Predicate<String> lowerCase = (s) -> Character.isLowerCase(s.charAt(0));
    
    public static void main(final String[] args) {
        filterWords(upperCase);
        filterWords(lowerCase);
    }

    public static List<String> filterWords(final Predicate<String> filter) {
        final List<String> result = new LinkedList<String>();
        for (final String word : words) {
            if (filter.isMatch(word)) {
                result.add(word);
            }
        }
        return result;
    }
}
