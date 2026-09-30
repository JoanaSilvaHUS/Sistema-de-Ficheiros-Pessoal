package pt.pa.adts.controller;

import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import pt.pa.Logger;
import pt.pa.adts.command.*;
import pt.pa.adts.exceptions.WrongDataInserted;
import pt.pa.adts.model.*;
import pt.pa.adts.model.File;
import pt.pa.adts.view.FileSystemUI;
import pt.pa.adts.view.PersonalFileSystemView;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * The {@code PersonalFileSystemController} class serves as the controller in the MVC pattern for the
 * Personal File System application. It manages user interactions, updates the model, and interacts with
 * the view.
 *
 * @author 202100316, 201801045, 202100190, 202100198
 */
public class PersonalFileSystemController {

    private PersonalFileSystemView view;

    private ActionManager actionManager;

    private PersonalFileSystem model;

    private static final DataFormat SERIALIZED_MIME_TYPE = new DataFormat("application/x-java-serialized-object");

    /**
     * Constructs a new {@code PersonalFileSystemController} with the specified view and model.
     *
     * @param view  The view associated with the controller.
     * @param model The model associated with the controller.
     */
    public PersonalFileSystemController(PersonalFileSystemView view, PersonalFileSystem model) {
        this.view = view;
        this.model = model;
        this.view.setTriggers(this);
        this.model.addObserver(this.view);
        this.actionManager = new ActionManager();
    }


    /**
     * Calls the command Add, creating a new file or directory.
     *
     * @throws PersonalFileSystemException If an error occurs during the operation.
     */
    public void doAddFileOrDirectory() throws PersonalFileSystemException {
        String nameToAdd = view.getTextFieldName();
        boolean isHidden = view.getSelectedIsHidden();
        boolean isLocked = view.getSelectedIsLocked();
        String content = view.getTextFieldContent();
        FileSystemItem parent = view.getSelectedDirectoryOrFile();
        FileSystemItem elementToAdd;

        if (view.getDirectoryIsSelected())
            elementToAdd = new Directory(nameToAdd);
        else
            elementToAdd = new File(nameToAdd, isLocked, isHidden, content);
        if (parent.isDirectory()) {
            try {
                actionManager.executeCommand(new AddCommand(model, elementToAdd, parent));
                Logger.log("Added: " + elementToAdd.getName() + " to " + parent.getName(), Logger.LogLevel.FILES);
                Logger.log("Added: " + elementToAdd.getName() + " to " + parent.getName(), Logger.LogLevel.ALL);
            } catch (PersonalFileSystemException e) {
                view.displayError(e.getMessage());
            }
        } else
            view.displayError("Não pode adicionar ficheiro/diretorias a um ficheiro");
    }


    /**
     * Calls the command to copy a file to another directory.
     *
     * @throws WrongDataInserted If incorrect data is inserted during the operation.
     */
    public void copyFile() throws WrongDataInserted {
            FileSystemItem parentToMove = view.getAlertSelectedDirectoryOrFile();
            FileSystemItem elementToCopy = view.getSelectedDirectoryOrFile();

            try {
                actionManager.executeCommand(new CopyCommand(model, elementToCopy, parentToMove));
                Logger.log("Copied: " + elementToCopy.getName() + " to " + parentToMove.getName(), Logger.LogLevel.FILES);
                Logger.log("Copied: " + elementToCopy.getName() + " to " + parentToMove.getName(), Logger.LogLevel.ALL);
            } catch (PersonalFileSystemException e) {
                view.displayError(e.getMessage());
            }
    }


    /**
     * Calls the command Move, moving a file to the desired directory.
     */
    public void moveFile(){
        FileSystemItem parentToMove = view.getAlertSelectedDirectoryOrFile();
        FileSystemItem itemToMove = view.getSelectedDirectoryOrFile();
        try{
            actionManager.executeCommand(new MoveCommand(model,itemToMove,parentToMove));
            Logger.log("Moved: " + itemToMove.getName() + " to " + parentToMove.getName(), Logger.LogLevel.FILES);
            Logger.log("Moved: " + itemToMove.getName() + " to " + parentToMove.getName(), Logger.LogLevel.ALL);
        }catch(PersonalFileSystemException e){
            view.displayError(e.getMessage());
        }
    }

    /**
     * Calls the command Remove, removing a file or a directory.
     */
    public void doRemoveFileOrDirectory() {

        FileSystemItem item = view.getSelectedDirectoryOrFile();

        try{
            actionManager.executeCommand(new RemoveCommand(model, item));
            Logger.log("Removed: " + item.getName(), Logger.LogLevel.DEL);
            Logger.log("Removed: " + item.getName(), Logger.LogLevel.ALL);
        }catch(PersonalFileSystemException e){
            view.displayError(e.getMessage());
        }
    }

    /**
     * Calls the command rename.
     */
    public void doRenameFileOrDirectory(){
        String newName = view.getTextFieldName();
        FileSystemItem item = view.getSelectedDirectoryOrFile();
        try{
            Logger.log("Renamed: " + item.getName() + " to " + newName, Logger.LogLevel.FILES);
            Logger.log("Renamed: " + item.getName() + " to " + newName, Logger.LogLevel.ALL);
            actionManager.executeCommand(new RenameCommand(model,newName,item));
        }catch(PersonalFileSystemException e){
            view.displayError(e.getMessage());
        }
    }

    /**
     * Calls the command to add an element from the clipboard using Ctrl+V.
     */
   public void doAddWithCtrlV() {
       FileSystemItem parent = view.getSelectedDirectoryOrFile();
       Clipboard clipboard = Clipboard.getSystemClipboard();
       if (clipboard.hasContent(SERIALIZED_MIME_TYPE)) {
           Object contentOfClipboard = clipboard.getContent(SERIALIZED_MIME_TYPE);
           if (contentOfClipboard instanceof FileSystemItem) {
               FileSystemItem item = (FileSystemItem) contentOfClipboard;
               for (FileSystemItem item1 : model.getFileSystem()) {
                   if (item1.getName().equals(item.getName())) {
                       item = item1;
                   }
               }
               actionManager.executeCommand(new CopyCommand(model, item, parent));
               Logger.log("Pasted: " + item.getName() + " to " + parent.getName(), Logger.LogLevel.FILES);
               Logger.log("Pasted: " + item.getName() + " to " + parent.getName(), Logger.LogLevel.ALL);
               clipboard.getFiles().clear();
           }

       }
   }

    /**
     * Copies the selected file or directory using Ctrl+C.
     */
    public void copyWithCtrlC() {
        FileSystemItem itemToCopy = view.getSelectedDirectoryOrFile();
        if (itemToCopy != null) {
            ClipboardContent  clipboardContent = new ClipboardContent();
            clipboardContent.put(SERIALIZED_MIME_TYPE, itemToCopy);
            Clipboard.getSystemClipboard().setContent(clipboardContent);
        }
    }

    /**
     * Export file to pc
     */

    public void export(){
        FileSystemItem itemToExport = view.getSelectedDirectoryOrFile();
        try {
                model.export(itemToExport);
        } catch(PersonalFileSystemException e){
            view.displayError(e.getMessage());
        }
    }

    /**
     * Initializes the display of the tree view.
     */
    public void execute(){
        view.initTreeViewDisplay();
    }


    public void saveToFile(){
        model.saveToFile(model);
    }
    /**
     *  unExecutes the last command done
     */
    public void undo(){
        actionManager.unExecuteCommand();
        Logger.log("Undid last command", Logger.LogLevel.ALL);
    }

}
