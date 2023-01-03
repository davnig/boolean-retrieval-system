package com.davnig.units.model.core;

import java.util.function.Consumer;

public class BinarySearchTree<T extends Comparable<T>> {

    class Node {
        T value;
        Node left, right;

        public Node(T value) {
            this.value = value;
            left = right = null;
        }
    }

    Node root;

    public BinarySearchTree() {
        root = null;
    }

    /**
     * Insert a new item in the tree.
     *
     * @param item the item of type {@link T} to be inserted
     */
    public void insert(T item) {
        root = traverseAndInsert(root, item);
    }

    /**
     * Perform inorder traversal and consume all the values of the tree.
     *
     * @param consumer the {@link Consumer} function to be applied on each item.
     */
    public void consume(Consumer<T> consumer) {
        traverseAndConsume(root, consumer);
    }

    /**
     * Delete the given item, if present.
     *
     * @param item the item of type {@link T} to be deleted
     */
    public void delete(T item) {
        root = traverseAndDelete(root, item);
    }

    private Node traverseAndInsert(Node root, T item) {
        // Return a new node if the tree is empty
        if (root == null) {
            root = new Node(item);
            return root;
        }
        // Traverse to the right place and insert the node
        if (item.compareTo(root.value) < 0)
            root.left = traverseAndInsert(root.left, item);
        else if (item.compareTo(root.value) > 0)
            root.right = traverseAndInsert(root.right, item);
        return root;
    }

    private void traverseAndConsume(Node root, Consumer<T> consumer) {
        if (root != null) {
            traverseAndConsume(root.left, consumer);
            consumer.accept(root.value);
            traverseAndConsume(root.right, consumer);
        }
    }

    private Node traverseAndDelete(Node root, T item) {
        // Return if the tree is empty
        if (root == null)
            return root;
        // Find the node to be deleted
        if (item.compareTo(root.value) < 0)
            root.left = traverseAndDelete(root.left, item);
        else if (item.compareTo(root.value) > 0)
            root.right = traverseAndDelete(root.right, item);
        else {
            // If the node is with only one child or no child
            if (root.left == null)
                return root.right;
            else if (root.right == null)
                return root.left;
            // If the node has two children
            // Place the inorder successor in position of the node to be deleted
            root.value = minValue(root.right);
            // Delete the inorder successor
            root.right = traverseAndDelete(root.right, root.value);
        }
        return root;
    }

    private T minValue(Node root) {
        T minv = root.value;
        while (root.left != null) {
            minv = root.left.value;
            root = root.left;
        }
        return minv;
    }

}
