package de.unistuttgart.iste.rss.oo.examples.bintree;

public class TreeTest {

    public static void main(final String[] args) {
        final TreeNode<Integer> intTree = new TreeNode<Integer>(
                new TreeNode<Integer>(1),
                new TreeNode<Integer>(3),
                2);
        System.out.println(intTree.toString());
    }

}
