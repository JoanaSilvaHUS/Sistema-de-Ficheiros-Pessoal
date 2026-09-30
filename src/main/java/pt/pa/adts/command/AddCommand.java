package pt.pa.adts.command;

import pt.pa.adts.model.Directory;
import pt.pa.adts.model.FileSystemItem;
import pt.pa.adts.model.PersonalFileSystem;

/**
 * The {@code AddCommand} class represents a command to add a {@code FileSystemItem}
 * (file or directory) to a specified parent directory within the file system.
 * It implements the {@code Command} interface and is part of a command pattern
 * implementation for a personal file system application.
 *
 * @author 202100316, 201801045, 202100190, 202100198
 */
public class AddCommand implements Command{

    /**
     * The {@code FileSystemItem} to be added to the file system.
     */
    private FileSystemItem elementToAdd;

    /**
     * The parent directory where {@code elementToAdd} will be added.
     */
    private FileSystemItem parentElement;

    /**
     * An instance of {@code PersonalFileSystem}, representing the model of the file system.
     */
    private PersonalFileSystem model;

    /**
     * Constructs an {@code AddCommand} with the specified parameters.
     *
     * @param model           The personal file system model.
     * @param elementToAdd    The {@code FileSystemItem} to be added.
     * @param parentDirectory The parent directory where the item will be added.
     */
    public AddCommand(PersonalFileSystem model, FileSystemItem elementToAdd, FileSystemItem parentDirectory){
        this.model = model;
        this.elementToAdd = elementToAdd;
        this.parentElement = parentDirectory;
    }

    /**
     * Executes the command, adding {@code elementToAdd} to the specified parent directory
     * in the file system. If the parent directory is not the root, it updates sizes accordingly.
     */
    @Override
    public void execute() {
        model.addElement(parentElement, elementToAdd);
    }


    /**
     * Undoes the executed command by removing the item from the file system and
     * updating sizes accordingly.
     */
    @Override
    public void unExecute() {
        model.removeElement(elementToAdd);
    }
}
