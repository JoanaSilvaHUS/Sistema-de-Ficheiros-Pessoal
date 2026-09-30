package pt.pa.adts.command;

import pt.pa.adts.model.FileSystemItem;
import pt.pa.adts.model.PersonalFileSystem;

import java.util.List;

/**
 * The {@code MoveCommand} class represents a command for moving a {@link FileSystemItem}
 * from its current location to a new destination within a {@link PersonalFileSystem}.
 * It implements the {@link Command} interface, allowing the operation to be executed
 * and undone.
 *
 * @author 202100316, 201801045, 202100190, 202100198
 */
public class MoveCommand implements Command{

    private FileSystemItem elementToMove;

    private FileSystemItem previousParent;

    private FileSystemItem destination;

    private PersonalFileSystem model;

    /**
     * Constructs a new {@code MoveCommand} with the specified parameters.
     *
     * @param model         The {@link PersonalFileSystem} instance.
     * @param elementToMove The {@link FileSystemItem} to be moved.
     * @param destination   The destination {@link FileSystemItem} where the element will be moved.
     */
    public MoveCommand(PersonalFileSystem model, FileSystemItem elementToMove, FileSystemItem destination){
        this.model = model;
        this.elementToMove = elementToMove;
        this.destination = destination;
        this.previousParent = model.findParent(elementToMove).element();

    }

    /**
     * Executes the move command, moving the element to the specified destination.
     * Updates the sizes of the destination and the previous parent accordingly.
     */
    @Override
    public void execute() {
        model.moveElement(elementToMove, destination);
    }

    /**
     * Undoes the move command, moving the element back to its previous parent.
     * Updates the sizes of the destination and the previous parent accordingly.
     */
    @Override
    public void unExecute() {
        model.moveElement(elementToMove, previousParent);

    }
}
