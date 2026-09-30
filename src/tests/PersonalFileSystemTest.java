import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.pa.adts.model.*;

import java.time.LocalDateTime;
import java.util.List;

public class PersonalFileSystemTest {

    private PersonalFileSystem personalFileSystem;
    private Directory rootDirectory;

    @BeforeEach
    void setUp() {
        rootDirectory = new Directory("Root");
        personalFileSystem = new PersonalFileSystem(rootDirectory);
    }

    @Test
    public void addElementTest() throws PersonalFileSystemException {
        Directory elementToAdd = new Directory("NewDirectory");

        personalFileSystem.addElement(rootDirectory, elementToAdd);

        List<Position<FileSystemItem>> children = personalFileSystem.findChildren(rootDirectory);

        Assertions.assertTrue(children.contains(personalFileSystem.findPosition(elementToAdd)));
    }

    @Test
    public void removeElementTest() throws PersonalFileSystemException {
        Directory parentDirectory = new Directory("Parent");
        Directory elementToRemove = new Directory("DirectoryToRemove");

        personalFileSystem.addElement(rootDirectory, parentDirectory);
        personalFileSystem.addElement(parentDirectory, elementToRemove);

        personalFileSystem.removeElement(elementToRemove);

        List<Position<FileSystemItem>> children = personalFileSystem.findChildren(parentDirectory);

        Assertions.assertFalse(children.contains(personalFileSystem.findPosition(elementToRemove)));
    }

    @Test
    public void moveElementTest() throws PersonalFileSystemException {
        Directory parentDirectory = new Directory("Parent");
        Directory destinationDirectory = new Directory("Destination");
        Directory elementToMove = new Directory("DirectoryToMove");

        personalFileSystem.addElement(rootDirectory, parentDirectory);
        personalFileSystem.addElement(parentDirectory, destinationDirectory);
        personalFileSystem.addElement(parentDirectory, elementToMove);

        personalFileSystem.moveElement(elementToMove, destinationDirectory);

        List<Position<FileSystemItem>> destinationChildren = personalFileSystem.findChildren(destinationDirectory);

        Assertions.assertTrue(destinationChildren.contains(personalFileSystem.findPosition(elementToMove)));
    }

    @Test
    public void renameElementTest() throws PersonalFileSystemException {
        Directory parentDirectory = new Directory("Parent");
        Directory elementToRename = new Directory("DirectoryToRename");

        personalFileSystem.addElement(rootDirectory, parentDirectory);
        personalFileSystem.addElement(parentDirectory, elementToRename);

        String newName = "RenamedDirectory";
        personalFileSystem.renameElement(elementToRename, newName);

        Assertions.assertEquals(newName, elementToRename.getName());
    }

    @Test
    public void copyElementTest() throws PersonalFileSystemException {
        Directory parentDirectory = new Directory("Parent");
        Directory elementToCopy = new Directory("DirectoryToCopy");

        personalFileSystem.addElement(rootDirectory, parentDirectory);
        personalFileSystem.addElement(parentDirectory, elementToCopy);

        Directory destinationDirectory = new Directory("Destination");
        personalFileSystem.addElement(parentDirectory, destinationDirectory);
        personalFileSystem.copyElement(elementToCopy, destinationDirectory);

        Assertions.assertEquals(1, destinationDirectory.getChildren().size());
    }

    @Test
    public void editElementTest() throws PersonalFileSystemException {
        // Create a file to edit
        File fileToEdit = new File("FileToEdit", false, false, "Initial Content");
        personalFileSystem.addElement(rootDirectory, fileToEdit);

        LocalDateTime initialDate = LocalDateTime.now().minusDays(1);
        // Save the last modification date before editing

        // Edit the file
        personalFileSystem.editElement(fileToEdit);

        // Verify that the last modification date has been updated
        Assertions.assertNotEquals(fileToEdit.getLastModificationDate(), initialDate);
    }
}
