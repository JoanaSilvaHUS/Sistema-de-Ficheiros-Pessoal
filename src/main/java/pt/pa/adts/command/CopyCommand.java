package pt.pa.adts.command;

import pt.pa.adts.model.Directory;
import pt.pa.adts.model.FileSystemItem;
import pt.pa.adts.model.PersonalFileSystem;

/**
 * The {@code CopyCommand} class represents a command for copying a {@link FileSystemItem}
 * from one location to another within a {@link PersonalFileSystem}.
 * It implements the {@link Command} interface, allowing the operation to be executed
 * and undone.
 *
 * @author 202100316, 201801045, 202100190, 202100198
 */
public class CopyCommand implements Command{
    private FileSystemItem elementToCopy;

    private FileSystemItem parentToMove;

    private FileSystemItem newElementCopy;

    private PersonalFileSystem model;

    /**
     * Constructs a new {@code CopyCommand} with the specified parameters.
     *
     * @param model         The {@link PersonalFileSystem} instance.
     * @param elementToCopy The {@link FileSystemItem} to be copied.
     * @param parentToMove  The destination {@link FileSystemItem} where the copy will be placed.
     */
    public CopyCommand(PersonalFileSystem model, FileSystemItem elementToCopy, FileSystemItem parentToMove){
        this.model = model;
        this.elementToCopy = elementToCopy;
        this.parentToMove = parentToMove;
    }

    /**
     * Executes the copy command, creating a copy of the element and placing it in the specified destination.
     * Updates the size of the destination to reflect the added size of the copied element.
     */
    @Override
    public void execute() {
      this.newElementCopy = model.copyElement(elementToCopy,parentToMove);
    }

    /**
     * Undoes the copy command, removing the previously copied element from the destination.
     * Updates the size of the destination to reflect the removed size of the copied element.
     */
    @Override
    public void unExecute() {
        model.removeElement(newElementCopy);
    }
}
