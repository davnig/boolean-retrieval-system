package com.davnig.units.model;

public class BinarySearchTree<T extends Comparable<T>> {

    class Node {
        T key;
        Node left, right;

        public Node(T item) {
            key = item;
            left = right = null;
        }
    }

    Node root;

    public BinarySearchTree() {
        root = null;
    }

    public void insert(T key) {
        root = insertKey(root, key);
    }

    /**
     * Insert a key in the tree
     *
     * @param root the root of the tree
     * @param key  the key to be inserted
     * @return {@link Node}
     */
    public Node insertKey(Node root, T key) {
        // Return a new node if the tree is empty
        if (root == null) {
            root = new Node(key);
            return root;
        }
        // Traverse to the right place and insert the node
        if (key.compareTo(root.key) < 0)
            root.left = insertKey(root.left, key);
        else if (key.compareTo(root.key) > 0)
            root.right = insertKey(root.right, key);
        return root;
    }

    public void inorder() {
        inorderRec(root);
    }

    public void deleteKey(T key) {
        root = deleteRec(root, key);
    }

    /**
     * Inorder Traversal
     *
     * @param root
     */
    private void inorderRec(Node root) {
        if (root != null) {
            inorderRec(root.left);
            System.out.print(root.key + " -> ");
            inorderRec(root.right);
        }
    }

    private Node deleteRec(Node root, T key) {
        // Return if the tree is empty
        if (root == null)
            return root;
        // Find the node to be deleted
        if (key.compareTo(root.key) < 0)
            root.left = deleteRec(root.left, key);
        else if (key.compareTo(root.key) > 0)
            root.right = deleteRec(root.right, key);
        else {
            // If the node is with only one child or no child
            if (root.left == null)
                return root.right;
            else if (root.right == null)
                return root.left;
            // If the node has two children
            // Place the inorder successor in position of the node to be deleted
            root.key = minValue(root.right);
            // Delete the inorder successor
            root.right = deleteRec(root.right, root.key);
        }
        return root;
    }

    /**
     * Find the inorder successor
     *
     * @param root
     * @return
     */
    private T minValue(Node root) {
        T minv = root.key;
        while (root.left != null) {
            minv = root.left.key;
            root = root.left;
        }
        return minv;
    }

}
