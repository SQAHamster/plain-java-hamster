package de.unistuttgart.iste.rss.oo.examples.bintree;

import java.util.Optional;

public class Main {

    public static void main(final String[] args) {
        final TreeNode<Integer> intTree = new TreeNode<Integer>(
                new TreeNode<Integer>(1),
                new TreeNode<Integer>(3),
                2);
        System.out.format("Overall tree sum is %d\n", computeSum(Optional.of(intTree)));
    }

    private static int computeSum(final Optional<TreeNode<Integer>> tree) {
        if (!tree.isPresent()) {
            return 0;
        }
        
        final TreeNode<Integer> currentNode = tree.get();
        int result = currentNode.getValue();
        result += computeSum(currentNode.getLeft());
        result += computeSum(currentNode.getRight());
        return result;
    }

}
