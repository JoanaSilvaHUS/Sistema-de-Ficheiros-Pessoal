package pt.pa.adts.model;

import pt.pa.adts.model.*;

import java.io.Serializable;
import java.util.*;

/**
 * @param <E> type of elements of the tree
 * @author patricia.macedo
 */
public class TreeLinked<E> implements Tree<E>, Serializable {

    //** TreeNode implemented as an inner class at the end **/

    private TreeNode root;

    public TreeLinked() {
        this.root = null;
    }

    public TreeLinked(E root) {
        this.root = new TreeNode(root);
    }

    /**
     * Returns the number of elements in the tree.
     *
     * @return The number of elements in the tree.
     */
    @Override
    public int size() {
        if (root == null) {
            return 0;
        } else {
            return size(root);
        }
    }

    /**
     * Returns the depth of the tree node at the specified position.
     *
     * @param position The position of the tree node.
     * @return The depth of the tree node.
     * @throws IllegalArgumentException If the position is not valid.
     */
    public int getDepth(Position<E> position) {
        TreeNode node = checkPosition(position);
        return getDepth(node);
    }

    /**
     * Recursively calculates and returns the depth of the specified tree node.
     *
     * @param treeNode The tree node for which to calculate the depth.
     * @return The depth of the tree node.
     */
    private int getDepth(TreeNode treeNode) {
        if (treeNode == null || treeNode == root) {
            return 0;
        }

        return 1 + getDepth(treeNode.parent);
    }

    /**
     * Recursively calculates and returns the size of the tree rooted at the specified tree node.
     *
     * @param treeNode The root of the tree for which to calculate the size.
     * @return The size of the tree rooted at the specified node.
     */
    public int size(TreeNode treeNode) {
        if (treeNode.children.isEmpty()) {
            return 1;
        } else {
            int s = 1;
            for (TreeNode child : treeNode.children) {
                s += size(child);
            }
            return s;
        }
    }

    /**
     * Returns true if the tree is empty, false otherwise.
     *
     * @return True if the tree is empty, false otherwise.
     */
    @Override
    public boolean isEmpty() {
        return root == null;
    }

    /**
     * Replaces the element at the specified position with the given element and returns the replaced element.
     *
     * @param position The position of the element to be replaced.
     * @param e        The new element to replace the existing one.
     * @return The element that was replaced.
     * @throws InvalidPositionException If the position is not valid.
     */
    @Override
    public E replace(Position<E> position, E e) throws InvalidPositionException {
        TreeNode node = checkPosition(position);
        E replacedElem = node.element;
        node.element = e;
        return replacedElem;
    }

    /**
     * Returns the position of the root of the tree.
     *
     * @return The position of the root of the tree.
     * @throws EmptyTreeException If the tree is empty.
     */
    @Override
    public Position<E> root() throws EmptyTreeException {
        if (isEmpty()) {
            throw new EmptyTreeException("The tree is empty.");
        }
        return root;
    }

    /**
     * Returns the position of the parent of the specified position in the tree.
     *
     * @param position The position for which to find the parent.
     * @return The position of the parent.
     * @throws InvalidPositionException     If the position is not valid.
     * @throws BoundaryViolationException    If the root has no parent.
     */
    @Override
    public Position<E> parent(Position<E> position) throws InvalidPositionException, BoundaryViolationException {
        TreeNode node = checkPosition(position);
        if (node == root) {
            throw new BoundaryViolationException("The root has no parent.");
        }
        return node.parent;
    }

    /**
     * Returns an iterable collection of the children of the specified position in the tree.
     *
     * @param position The position for which to find the children.
     * @return An iterable collection of the children.
     * @throws InvalidPositionException If the position is not valid.
     */
    @Override
    public Iterable<Position<E>> children(Position<E> position) throws InvalidPositionException {
        TreeNode node = checkPosition(position);
        ArrayList<Position<E>> list = new ArrayList<>();
        for (Position<E> pos : node.children) {
            list.add(pos);
        }
        return list;
    }

    /**
     * Returns true if the specified position represents an internal node, false otherwise.
     *
     * @param position The position to check.
     * @return True if the position is an internal node, false otherwise.
     * @throws InvalidPositionException If the position is not valid.
     */
    @Override
    public boolean isInternal(Position<E> position) throws InvalidPositionException {
        TreeNode node = checkPosition(position);
        return !node.children.isEmpty();
    }

    /**
     * Returns true if the specified position represents an external node, false otherwise.
     *
     * @param position The position to check.
     * @return True if the position is an external node, false otherwise.
     * @throws InvalidPositionException If the position is not valid.
     */
    @Override
    public boolean isExternal(Position<E> position) throws InvalidPositionException {
        TreeNode aux = checkPosition(position);
        return aux.children.isEmpty();
    }

    /**
     * Returns true if the specified position represents the root of the tree, false otherwise.
     *
     * @param position The position to check.
     * @return True if the position is the root of the tree, false otherwise.
     * @throws InvalidPositionException If the position is not valid.
     */
    @Override
    public boolean isRoot(Position<E> position) throws InvalidPositionException {
        TreeNode aux = checkPosition(position);
        return this.root == aux;
    }

    /**
     * Inserts a new element as a child of the specified parent position in the tree.
     *
     * @param parent The position where the new element will be inserted as a child.
     * @param elem   The element to be inserted.
     * @return The position of the newly inserted element.
     * @throws BoundaryViolationException If the parent is not null (for the root of the tree).
     * @throws InvalidPositionException   If the parent position is not valid.
     */
    @Override
    public Position<E> insert(Position<E> parent, E elem)
            throws BoundaryViolationException, InvalidPositionException {
        if (isEmpty()) {
            if (parent != null) {
                throw new InvalidPositionException("Parent must be null for the root of the tree.");
            }
            this.root = new TreeNode(elem);
            return root;
        }

        TreeNode parentNode = checkPosition(parent);
        TreeNode node = new TreeNode(elem, parentNode);
        parentNode.children.add(node);
        return node;
    }

    /**
     * Inserts a new element with a specified order as a child of the specified parent position in the tree.
     *
     * @param parent The position where the new element will be inserted as a child.
     * @param elem   The element to be inserted.
     * @param order  The order at which the element should be inserted among the children of the parent.
     * @return The position of the newly inserted element.
     * @throws BoundaryViolationException If the parent is not null (for the root) or if the order is out of bounds.
     * @throws InvalidPositionException   If the parent position is not valid.
     */
    @Override
    public Position<E> insert(Position<E> parent, E elem, int order)
            throws BoundaryViolationException, InvalidPositionException {
        if (isEmpty()) {
            if (parent != null) {
                throw new InvalidPositionException("Parent must be null for the root");
            }
            if (order != 0) {
                throw new BoundaryViolationException("Order must be 0 for the root");
            }
            this.root = new TreeNode(elem);
            return root;
        }

        TreeNode parentNode = checkPosition(parent);

        TreeNode newNode = new TreeNode(elem, parentNode);

        if (order < 0 || order > parentNode.children.size()) {
            throw new BoundaryViolationException("Order is out of bounds");
        }

        if (order == parentNode.children.size()) {
            parentNode.children.add(newNode);
        } else {
            parentNode.children.add(order, newNode);
        }

        return newNode;
    }

    /**
     * Removes the element at the specified position from the tree and returns the removed element.
     *
     * @param position The position of the element to be removed.
     * @return The element that was removed.
     * @throws InvalidPositionException If the position is not valid.
     */
    @Override
    public E remove(Position<E> position) throws InvalidPositionException {
        TreeNode nodeToRemove = checkPosition(position);
        E removedElement = nodeToRemove.element;

        if (nodeToRemove == root) {
            if (nodeToRemove.children.isEmpty()) {
                root = null;
            } else {
                TreeNode newRoot = nodeToRemove.children.get(0);
                newRoot.parent = null;
                root = newRoot;
            }
        } else {
            nodeToRemove.parent.children.remove(nodeToRemove);
        }

        return removedElement;
    }

    /**
     *   auxiliary method to check if Position is valid and cast to a treeNode
     */
    private TreeNode checkPosition(Position<E> position)
            throws InvalidPositionException {
        if (position == null) {
            throw new InvalidPositionException();
        }

        try {
            TreeNode treeNode = (TreeNode) position;
            if (treeNode.children == null) {
                throw new InvalidPositionException("The position is invalid");
            }
            return treeNode;
        } catch (ClassCastException e) {
            throw new InvalidPositionException();
        }
    }

    /**
     * Returns an iterable collection of positions in the tree, following a pre-order traversal.
     * The iterator visits each position in the tree, starting from the root.
     *
     * @return An iterable collection of positions in the tree.
     */
    @Override
    public Iterable<Position<E>> positions() {
        ArrayList<Position<E>> list = new ArrayList<>();
        if (!isEmpty()) {
            positions(root, list);
        }
        return list;
    }

    /**
     * auxiliary recursive method for elements() method
     **/
    private void elements(Position<E> position, ArrayList<E> lista) {
        lista.add(lista.size(), position.element()); // visit (position) primeiro, pre-order
        for (Position<E> w : children(position)) {
            elements(w, lista);
        }
    }

    /**
     * Returns an iterable collection of elements in the tree, following a pre-order traversal.
     * The iterator visits each element in the tree, starting from the root.
     *
     * @return An iterable collection of elements in the tree.
     */
    @Override
    public Iterable<E> elements() {
        ArrayList<E> list = new ArrayList<>();
        if (!isEmpty()) {
            elements(root, list);
        }
        return list;
    }

    /**
     * auxiliary recursive method for positions() method
     **/
    private void positions(Position<E> position, ArrayList<Position<E>> list) {
        for (Position<E> w : children(position)) {
            positions(w, list);
        }
        list.add(list.size(), position); // visit (position)
    }

    /**
     * Calculates and returns the height of the tree rooted at the specified node.
     * The height of a tree is the length of the longest path from the root to a leaf.
     * If the tree is empty or the root is null, -1 is returned.
     *
     * @param treeRoot The root of the tree for which to calculate the height.
     * @return The height of the tree rooted at the specified node.
     */
    private int height(TreeNode treeRoot) {
        if (treeRoot == null) return -1;
        if (treeRoot.children.isEmpty()) return 0; /* important */

        /* select maximum value */
        int childMaxHeight = Integer.MIN_VALUE;
        for (TreeNode childRoot : treeRoot.children) {
            int childHeight = height(childRoot);
            if (childHeight > childMaxHeight) {
                childMaxHeight = childHeight;
            }
        }
        return 1 + childMaxHeight;
    }

    /**
     * Calculates and returns the height of the entire tree.
     * The height of a tree is the length of the longest path from the root to a leaf.
     * If the tree is empty, -1 is returned.
     *
     * @return The height of the entire tree.
     */
    public int height() {
        return height(this.root);
    }

    /**
     * Move (remove e insere) um nó da árvore e torna-o descendente de outro nó.
     * @param existingPosition Elemento a mover
     * @param newParent Destino
     */
    @Override
    public void move(Position<E> existingPosition, Position<E> newParent) {
        TreeNode oldParentNode = checkPosition(parent(existingPosition));
        TreeNode newParentNode = checkPosition(newParent);

        oldParentNode.children.remove(existingPosition);

        newParentNode.children.add((TreeNode)existingPosition);
        ((TreeNode) existingPosition).parent = newParentNode;
    }

    /**
     * Checks if a given position is an ancestor of another position in the tree.
     * An ancestor is a node that is on the path from the root to the descendant.
     *
     * @param posDesc The possible descendant position.
     * @param posAsc  The possible ascendant position.
     * @return True if the posAsc is an ancestor of posDesc, false otherwise.
     */
    @Override
    public boolean isAncestor(Position<E> posDesc, Position<E> posAsc) {
        if (posDesc == null || posAsc == null) return false;

        TreeNode descendantNode = checkPosition(posDesc);  // possible descendant
        TreeNode ascendantNode = checkPosition(posAsc);    // possible ascendant

        if (isRoot(descendantNode)) return false;           // the root has no ancestors
        if (descendantNode.parent == ascendantNode) return true;
        return isAncestor(descendantNode.parent, ascendantNode);
    }

    /**
     * Generates a string representation of the tree using pre-order traversal.
     * The result is a comma-separated list of elements visited in pre-order.
     *
     * @param position The position to start the pre-order traversal.
     * @return A string representation of the tree using pre-order traversal.
     */
    private String toStringPreOrder(Position<E> position) {
        StringBuilder sb = new StringBuilder(position.element().toString()); // visit (position)

        for (Position<E> w : children(position)) {
            sb.append(",").append(toStringPreOrder(w));
        }
        return sb.toString();
    }

    /**
     * Returns a string representation of the tree using a level-based pre-order traversal.
     * Each level is separated by a newline character, and elements at the same level are comma-separated.
     *
     * @return A string representation of the tree using level-based pre-order traversal.
     */
    public String toString() {
        String str = "";
        if (!isEmpty()) {
            str = toStringPreOrderLevels(root, 1);
        }
        return str;
    }

    /**
     * Returns the degree of a given position, which is the number of children the position has.
     *
     * @param position The position for which to determine the degree.
     * @return The degree of the given position.
     * @throws InvalidPositionException If the position is not valid for this tree.
     */
    @Override
    public int degree(Position<E> position) throws InvalidPositionException {
        TreeNode node = checkPosition(position);
        return node.children.size();
    }

    /**
     *  auxiliary method to write Tree, using preorder approach
     *  */
    private String toStringPreOrderLevels(Position<E> position, int level) {
        StringBuilder sb = new StringBuilder(position.element().toString()); // visit (position)

        for (Position<E> w : children(position)) {
            sb.append("\n").append(printLevel(level)).append(toStringPreOrderLevels(w, level + 1));
        }
        return sb.toString();
    }

    /**
     * auxiliary method to format a level of the tree
     */
    private String printLevel(int level) {
        return Collections.nCopies(level,"  ") + "-";
    }

    /**
     * inner class - represent a node of a tree. Each node have a list of children, that can be empty.
     */
    private class TreeNode implements Position<E>, Serializable {

        private E element;  // element stored at this node
        private TreeNode parent;  // adjacent node
        private List<TreeNode> children;  // children nodes

        TreeNode(E element) {
            this.element = element;
            parent = null;
            children = new ArrayList<>();
        }

        TreeNode(E element, TreeNode parent) {
            this.element = element;
            this.parent = parent;
            this.children = new ArrayList<>();
        }

        public E element() {
            if (element == null) {
                throw new InvalidPositionException();
            }
            return element;
        }

    }

    /**
     * Returns an iterator over the elements of the tree. The iterator follows a pre-order traversal.
     *
     * @return An iterator over the elements of the tree.
     */
    public Iterator<E> iterator(){
        return new MyIterator(root);
    }

    /**
     * Inner class representing an iterator for the tree elements. The iterator follows a pre-order traversal.
     */
    private class MyIterator implements Iterator<E> {
        private Stack<TreeNode> stack;

        /**
         * Constructs a new iterator with the specified root.
         *
         * @param root The root of the tree to iterate over.
         */
        public MyIterator(TreeNode root){
            stack = new Stack<>();
            if(root!=null){
                stack.push(root);
            }
        }

        /**
         * Checks if there are more elements in the tree to iterate over.
         *
         * @return {@code true} if there are more elements, {@code false} otherwise.
         */
        @Override
        public boolean hasNext(){
            return !stack.isEmpty();
        }

        /**
         * Returns the next element in the iteration.
         *
         * @return The next element in the iteration.
         * @throws EmptyTreeException If the tree contains no more elements.
         */
        @Override
        public E next(){
            if (!hasNext()) {
                throw new EmptyTreeException("A árvore não contém mais elementos");
            }

            TreeNode current = stack.pop();
            for (int i = current.children.size() - 1; i >= 0; i--) {
                stack.push(current.children.get(i));
            }

            return current.element();
        }

    }
}
