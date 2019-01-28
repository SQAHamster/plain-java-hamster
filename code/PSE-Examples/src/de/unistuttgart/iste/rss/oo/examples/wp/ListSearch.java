package de.unistuttgart.iste.rss.oo.examples.wp;

import java.util.ArrayList;
import java.util.List;

public class ListSearch {

    public static void main(final String[] args) {
        final List<Integer> l = initList();
        System.out.println(findMinIndex(l, 5));
    }

    private static <G> int findMinIndex(final List<G> l, final G a) {
        for (int i = 0; i < l.size(); i++) {
            if (l.get(i) == a) {
                return i;
            }
        }
        throw new IllegalArgumentException();
    }

    private static List<Integer> initList() {
        final List<Integer> result = new ArrayList<>(1001);
        for (int i = 0; i <= 1000; i++) {
            result.add(i, (int)(Math.floor(20 * Math.random())));
        }
        return result;
    }

}
