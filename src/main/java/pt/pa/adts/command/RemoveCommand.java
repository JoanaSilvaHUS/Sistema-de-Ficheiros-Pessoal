package pt.pa.adts.command;

import pt.pa.adts.model.*;

/**
 * The {@code RemoveCommand} class represents a command for removing a {@link FileSystemItem}
 * from a {@link PersonalFileSystem}. It implements the {@link Command} interface, allowing
 * the operation to be executed and undone.
 *
 * <p>The command supports the removal of an entire subtree starting from the specified element.
 * The removed elements and their structure are stored in a supplementary tree for undo operations.
 *
 * @author 202100316, 201801045, 202100190, 202100198
 */
public class RemoveCommand implements Command{
    private FileSystemItem elementToRemove;

    private FileSystemItem parentElement;

    private PersonalFileSystem model;

    private TreeLinked<FileSystemItem> removedTree;

    /**
     * Constructs a new {@code RemoveCommand} with the specified parameters.
     *
     * @param model          The {@link PersonalFileSystem} instance.
     * @param elementToRemove The {@link FileSystemItem} to be removed.
     */
    public RemoveCommand(PersonalFileSystem model, FileSystemItem elementToRemove){
        this.model = model;
        this.elementToRemove = elementToRemove;
        this.parentElement = model.findParent(elementToRemove).element();
        this.removedTree = new TreeLinked<>(elementToRemove);
        supplementaryAdd(removedTree.root());
    }

    /**
     * Executes the remove command, removing the specified element and its subtree.
     * Updates sizes and removes elements from the model accordingly.
     */
    @Override
    public void execute() {
        supplementaryRemove(model.findPosition(elementToRemove));
        model.removeElement(elementToRemove);
    }

    /**
     * Undoes the remove command, adding the removed element and its subtree back to the model.
     * Restores sizes for undo operations.
     */
    @Override
    public void unExecute() {
        model.addElement(parentElement,elementToRemove);
        supplementaryAddUndo(removedTree.root());
    }

    /**
     * Adds supplementary nodes to the removal tree recursively.
     *
     * @param e The position in the removal tree.
     */
    private void supplementaryAdd(Position<FileSystemItem> e){
        for (Position<FileSystemItem> c : model.findChildren(e.element())) {
            Position<FileSystemItem> ya = removedTree.insert(e,c.element());
            supplementaryAdd(ya);
        }
    }

    /**
     * Recursively removes elements from the model and their corresponding positions.
     *
     * @param e The position in the removal tree.
     */
    private void supplementaryRemove(Position<FileSystemItem> e){
        for (Position<FileSystemItem> c : model.findChildren(e.element())) {
            supplementaryRemove(c);
            model.removeElement(c.element());
        }
    }


    /**
     * Adds the removed elements back to the model during undo, restoring their structure.
     *
     * @param parent The root position in the removal tree.
     */
    private void supplementaryAddUndo(Position<FileSystemItem> parent){

        for (Position<FileSystemItem> child : removedTree.children(parent)) {
            model.addElement(parent.element(),child.element());
            supplementaryAddUndo(child);
        }
    }


}
