package de.unistuttgart.iste.rss.oo.examples.bintree;

import java.util.Objects;
import java.util.Optional;

public final class TreeNode<G> {

    private final Optional<TreeNode<G>> left;
    private final Optional<TreeNode<G>> right;
    private final G value;
    
    /** Creates a node in a binary tree with the given value and child trees.
     * @param left Left subtree.
     * @param right Right subtree.
     * @param value Value stored in this node.
     */
    public TreeNode(final Optional<TreeNode<G>> left, final Optional<TreeNode<G>> right, final G value) {
        super();
        Objects.nonNull(left);
        Objects.nonNull(right);
        Objects.nonNull(value);
        this.left = left;
        this.right = right;
        this.value = value;
    }
    
    /** Convenience overloaded constructor which creates a leaf node.
     * @param value Value of this leave node.
     */
    public TreeNode(final G value) {
        this(Optional.empty(), Optional.empty(), value);
    }

    /** Creates a node in a binary tree with the given value and child trees.
     * @param left Left subtree.
     * @param right Right subtree.
     * @param value Value stored in this node.
     */
    public TreeNode(final TreeNode<G> left, final TreeNode<G> right, final G value) {
        this(Optional.of(left), Optional.of(right), value);
    }

    @Override
    public String toString() {
        final StringBuffer result = new StringBuffer();
        result.append("<");
        if (left.isPresent()) {
            result.append(left.get().toString());
        }
        result.append("[");
        result.append(value.toString());
        result.append("]");
        if (right.isPresent()) {
            result.append(right.get().toString());
        }
        result.append(">");
        return result.toString();
    }

    public G getValue() {
        return value;
    }

    public Optional<TreeNode<G>> getLeft() {
        return left;
    }

    public Optional<TreeNode<G>> getRight() {
        return right;
    }
    
}
