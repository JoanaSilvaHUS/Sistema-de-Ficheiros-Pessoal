package pt.pa.adts.command;

import pt.pa.adts.model.*;

/**
 * The {@code RenameCommand} class represents a command for renaming a {@link FileSystemItem}
 * within a {@link PersonalFileSystem}. It implements the {@link Command} interface, allowing
 * the operation to be executed and undone.
 *
 * <p>The command stores the old and new names of the element being renamed, and it delegates
 * the renaming operation to the {@link PersonalFileSystem}.
 *
 * @author 202100316, 201801045, 202100190, 202100198
 */
public class RenameCommand implements Command{
    private String newName;
    private String oldName;
    private FileSystemItem elementToRename;
    private PersonalFileSystem personalFileSystem;

    /**
     * Constructs a new {@code RenameCommand} with the specified parameters.
     *
     * @param personalFileSystem The {@link PersonalFileSystem} instance.
     * @param newName            The new name for the element.
     * @param elementToRename    The {@link FileSystemItem} to be renamed.
     */
    public RenameCommand(PersonalFileSystem personalFileSystem, String newName, FileSystemItem elementToRename){
        this.personalFileSystem = personalFileSystem;
        this.newName = newName;
        this.elementToRename = elementToRename;
        this.oldName = this.elementToRename.getName();
    }

    /**
     * Executes the rename command, updating the name of the specified element.
     */
    @Override
    public void execute() {
        personalFileSystem.renameElement(this.elementToRename, this.newName);
    }

    /**
     * Undoes the rename command, reverting the name of the specified element to its old name.
     */
    @Override
    public void unExecute() {
        personalFileSystem.renameElement(this.elementToRename, this.oldName);
    }
}
