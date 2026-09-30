package pt.pa.adts.model;



import pt.pa.adts.observer.Subject;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import java.util.List;

/**
 * Represents a personal file system with hierarchical organization.
 * This class provides methods to manipulate the file system, such as adding, removing,
 * moving, copying, renaming, and editing elements. It also allows observing changes in the
 * file system through the use of observers.
 *
 * @author 202100316, 201801045, 202100190, 202100198
 */
public class PersonalFileSystem extends Subject implements Serializable{

    Position<FileSystemItem> directory;
    TreeLinked<FileSystemItem> fileSystem;
    List<Position<FileSystemItem>> fileSystemPositions;

    /**
     * Constructs a PersonalFileSystem with the specified root directory.
     *
     * @param directory The root directory to be used for the personal file system.
     */
    public PersonalFileSystem(Directory directory){
        this.fileSystem = new TreeLinked<>(directory);
        this.fileSystemPositions = new ArrayList<>();
        this.directory = fileSystem.root();
        fileSystemPositions.add(this.directory);
    }

    /**
     * Add a file or directory and notify all observers
     * @param parentDirectory
     * @param elementToAdd
     */
    public void addElement(FileSystemItem parentDirectory, FileSystemItem elementToAdd) throws PersonalFileSystemException {

        Position<FileSystemItem> parent = findPosition(parentDirectory);

        if(parent == null)
            throw new PersonalFileSystemException("Inserted parent directory does not exist");

        Position<FileSystemItem> position = fileSystem.insert(parent, elementToAdd);
        Directory directory1 = (Directory) findPosition(parentDirectory).element();
        directory1.addChildren(elementToAdd);
        fileSystemPositions.add(position);
        notifyObservers(elementToAdd);
        addElementToParents(parent,elementToAdd);
        updateFileSizes();
    }

    /**
     * Adds a file or directory to the file system and notifies all observers.
     *
     * @param parentDirectory The parent directory to which the element will be added.
     * @param elementsToAdd    The element to add.
     * @throws PersonalFileSystemException If the parent directory doesn't exist.
     */
    public void addElements(FileSystemItem parentDirectory, FileSystemItem... elementsToAdd) throws PersonalFileSystemException {

        Position<FileSystemItem> parent = findPosition(parentDirectory);
        Directory directory1 = (Directory) findPosition(parentDirectory).element();
        if(parent == null)
            throw new PersonalFileSystemException("Inserted parent directory does not exist");

        for (FileSystemItem element : elementsToAdd) {
            Position<FileSystemItem> position = fileSystem.insert(parent, element);
            directory1.addChildren(element);
            fileSystemPositions.add(position);
            notifyObservers(element);
            addElementToParents(parent,element);
            updateFileSizes();
        }
    }

    /**
     * Recursively adds the specified {@code FileSystemItem} element to all its parent directories.
     *
     * @param currentParent The position of the current parent directory where the element should be added.
     * @param elementToAdd  The {@code FileSystemItem} element to be added to its parent directories.
     */
    private void addElementToParents(Position<FileSystemItem> currentParent, FileSystemItem elementToAdd) {
        Position<FileSystemItem> parent = findParent(currentParent.element());

        if (parent != null) {
            Directory parentDirectory = (Directory) parent.element();
            if(!parentDirectory.getChildren().contains(elementToAdd)){
            parentDirectory.addChildren(elementToAdd);}
            addElementToParents(parent,elementToAdd);
        }
    }

    /**
     * Removes the specified FileSystemItem from all parent directories recursively.
     *
     * @param currentParent The current parent position to start the removal process.
     * @param elementToRemove The FileSystemItem to be removed from its parent directories.
     */
    private void removeElementToParents(Position<FileSystemItem> currentParent, FileSystemItem elementToRemove) {
        if (currentParent != null) {
            Directory parentDirectory = (Directory) currentParent.element();
            if (parentDirectory.getChildren().contains(elementToRemove)) {
                parentDirectory.removeChildren(elementToRemove);
            }

            Position<FileSystemItem> parent = findParent(currentParent.element());
            removeElementToParents(parent, elementToRemove);
        }
    }

    /**
     * Removes the specified FileSystemItem and its descendants from the file system.
     * Notifies registered observers after the removal.
     *
     * @param elementToRemove The FileSystemItem to be removed from the file system.
     */
    public void removeElement(FileSystemItem elementToRemove){
        Position<FileSystemItem> element = findPosition(elementToRemove);
        Position<FileSystemItem> parentDirectory = findParent(elementToRemove);
        Directory directory1 = (Directory) findPosition(parentDirectory.element()).element();
        if(!findChildren(elementToRemove).isEmpty()){
            for (Position<FileSystemItem> item: findChildren(elementToRemove)) {
                    directory1.removeChildren(item.element());
                    removeElementToParents(findParent(item.element()),item.element());
                    removeElement(item.element());
            }
        }
        directory1.removeChildren(elementToRemove);
        removeElementToParents(parentDirectory,elementToRemove);
        fileSystemPositions.remove(element);
        fileSystem.remove(element);
        notifyObservers(elementToRemove);
        updateFileSizes();
    }

    /**
     * Create empty element with just a name
     * @param parentDirectory
     * @param fileName
     * @param type
     * @throws PersonalFileSystemException
     */
    public void createElement(FileSystemItem parentDirectory, String fileName, FileSystemItem.Type type) throws PersonalFileSystemException {
        boolean isDirectory = false;

        if(type == FileSystemItem.Type.Directory)
            isDirectory = true;

        addElement(parentDirectory,new FileSystemItem(fileName,isDirectory));
    }

    /**
     * Retrieves a list of directories from the file system.
     *
     * @return A list of directories.
     */
    public List<FileSystemItem> getDirectories(){
        List<FileSystemItem> directoryList = new ArrayList<>();
        for (FileSystemItem f : getFileSystem()){
            if(f.isDirectory()){
                directoryList.add(f);
            }
        }
        return directoryList;
    }

    /**
     * Retrieves a list of files from the file system.
     *
     * @return A list of directories.
     */
    public List<FileSystemItem> getFiles(){
        List<FileSystemItem> fileList = new ArrayList<>();
        for (FileSystemItem f : getFileSystem()){
            if(!f.isDirectory()){
                fileList.add(f);
            }
        }
        return fileList;
    }

    /**
     * Counts the number of descendants (children) for a given {@code FileSystemItem}.
     *
     * @param s The {@code FileSystemItem} for which descendants are counted.
     * @return The number of descendants.
     */
    public int countDescendants(FileSystemItem s){
        List<Position<FileSystemItem>> descendants = findChildren(s);
        return descendants.size();
    }

    /**
     * Moves the specified FileSystemItem to the given destination directory in the file system.
     * If the element to move is a directory, it recursively moves its descendants as well.
     * Notifies registered observers after the move operation.
     *
     * @param elementToMove        The FileSystemItem to be moved.
     * @param destinationDirectory The destination directory where the element will be moved.
     * @throws PersonalFileSystemException If an error occurs during the move operation.
     */
    public void moveElement( FileSystemItem elementToMove, FileSystemItem destinationDirectory) throws PersonalFileSystemException {

        if(findPosition(elementToMove).element().isDirectory()){
            //remove from tree
            fileSystem.remove(findPosition(elementToMove));

            //remove from parent children
            Directory parent = (Directory) findParent(elementToMove).element();
            for (FileSystemItem child: parent.getChildren()) {
                parent.removeChildren(child);
                removeElementToParents(findParent(elementToMove),child);
            }
            parent.removeChildren(elementToMove);
            removeElementToParents(findParent(elementToMove),elementToMove);


            //insert on tree on new position
            Position<FileSystemItem> newPosition = fileSystem.insert(findPosition(destinationDirectory),elementToMove);

            //remove from positions
            fileSystemPositions.remove(findPosition(elementToMove));

            //add new position
            fileSystemPositions.add(newPosition);

            //add to children of new parent
            Directory parentDirectory = (Directory) destinationDirectory;
            parentDirectory.addChildren(elementToMove);
            addElementToParents(findParent(elementToMove),elementToMove);


            Directory elementDirectory = (Directory) elementToMove;
            moveElements(elementDirectory.getChildren(),elementToMove);


        }else{
            fileSystem.remove(findPosition(elementToMove));

            //remove from parent children
            Directory parent = (Directory) findParent(elementToMove).element();
            parent.removeChildren(elementToMove);
            removeElementToParents(findParent(elementToMove),elementToMove);

            //remove from positions
            fileSystemPositions.remove(findPosition(elementToMove));

            //insert on tree on new position
            Position<FileSystemItem> newPosition = fileSystem.insert(findPosition(destinationDirectory),elementToMove);

            //add new position
            fileSystemPositions.add(newPosition);

            //add to children of new parent
            Directory parentDirectory = (Directory) destinationDirectory;
            parentDirectory.addChildren(elementToMove);
            addElementToParents(findParent(elementToMove),elementToMove);

        }

        notifyObservers(elementToMove);
        updateFileSizes();
    }

    /**
     * Moves a list of FileSystemItems to the given destination directory in the file system.
     * Recursively moves descendants if the elements to move are directories.
     * Notifies registered observers after each move operation.
     *
     * @param elementsToMove       The list of FileSystemItems to be moved.
     * @param destinationDirectory The destination directory where the elements will be moved.
     */
    public void moveElements(List<FileSystemItem> elementsToMove, FileSystemItem destinationDirectory){

        for(FileSystemItem child : elementsToMove){

            fileSystem.remove(findPosition(child));
            //insert on tree on new position
            Position<FileSystemItem> newPosition = fileSystem.insert(findPosition(destinationDirectory),child);

            //remove from positions
            fileSystemPositions.remove(findPosition(child));
            //add new position
            fileSystemPositions.add(newPosition);
            Directory parentDirectory = (Directory) destinationDirectory;
            parentDirectory.addChildren(child);
            addElementToParents(findPosition(destinationDirectory),child);

            if(child.isDirectory()) {
                Directory childDirectory = (Directory) child;
                if(!childDirectory.getChildren().isEmpty())
                    moveElements(childDirectory.getChildren(), child);
            }

            notifyObservers(child);
            updateFileSizes();
        }
    }

    /**
     * Rename an element
     * @param elementToRename
     * @param renameString
     * @throws PersonalFileSystemException
     */
    public void renameElement(FileSystemItem elementToRename,String renameString) throws PersonalFileSystemException {

        Position<FileSystemItem> element = findPosition(elementToRename);

        if(element == null)
            throw new PersonalFileSystemException("Inserted element does not exist");

        element.element().setName(renameString);
        notifyObservers(element);
    }

    /**
     * Copy an element to another directory
     * @param elementToCopy
     * @param destinationDirectory
     * @throws PersonalFileSystemException
     */
    public FileSystemItem copyElement(FileSystemItem elementToCopy, FileSystemItem destinationDirectory) throws PersonalFileSystemException {

        Position<FileSystemItem> element = findPosition(elementToCopy);
        Position<FileSystemItem> destination = findPosition(destinationDirectory);
        Directory destination1 = (Directory) destinationDirectory;

        if(element == null || destination == null)
            throw new PersonalFileSystemException();
        if(!destination.element().isDirectory())
            throw new PersonalFileSystemException("It must be a directory");

        String newName = "";
        int number = getNumberOfCopy(elementToCopy.getName());

        if(number == -1)
            newName += ("(" + 1 + ")");
         else
             newName += ("(" + (number + 1) + ")");

        newName =  fileNameWithoutParenthesis(elementToCopy.getName()) + newName;

        FileSystemItem newCopy;
        if(elementToCopy.isDirectory()) {
            newCopy = new Directory(newName);
            newCopy.resetSize();
        }
        else
            newCopy = new File(newName, elementToCopy.isLocked(), elementToCopy.isHidden(), elementToCopy.getContent());


        addElement(destinationDirectory, newCopy);
        if(!destination1.getChildren().contains(newCopy)){
        destination1.addChildren(newCopy);}
        addElementToParents(findPosition(destination1),newCopy);
        copyElements(findChildren(elementToCopy),newCopy);
        notifyObservers(element);
        return newCopy;
    }

    /**
     * Copies a list of FileSystemItems to the given destination directory in the file system.
     * Notifies registered observers after each copy operation.
     *
     * @param elementsToMove       The list of Positions representing FileSystemItems to be copied.
     * @param destinationDirectory The destination directory where the elements will be copied.
     */
    private void copyElements(List<Position<FileSystemItem>> elementsToMove, FileSystemItem destinationDirectory){
        for(Position<FileSystemItem> child : elementsToMove){
            Directory directory1 = (Directory) destinationDirectory;
            copyElement(child.element(), destinationDirectory);
            notifyObservers(child.element());
        }
    }

    /**
     * Update Size of the files
     */
    public void updateFileSizes(){
        for (FileSystemItem item : fileSystem) {
            setSizes(item);
        }
    }

    /**
     * Sets sizes recursively starting from the specified element.
     *
     * @param item The starting {@link FileSystemItem}.
     */
    private void setSizes(FileSystemItem item){
        Directory parent = null;
        if(item!=null && !item.equals(fileSystem.root().element())) {
            parent = (Directory) findParent(item).element();
            parent.resetSize();
        }
        if(item.isDirectory()){
            Directory directory1 = (Directory) item;
            if(directory1.getChildren().isEmpty()){
            item.resetSize();}
        }
        if (parent != null) {
            for (FileSystemItem it : parent.getChildren()) {
                if (it.isDirectory() == false) {
                    parent.addToSize(it.getSizeInBytes());
                }
            }
            setSizes(parent);
        }
    }

    /**
     * Auxiliar method to return the number of the file
     * @param fileName
     * @return -1 if does not contain a number between parenthesis
     * or if found return the number between them
     */
    private int getNumberOfCopy(String fileName){

        int startIndex = fileName.indexOf("(");
        int endIndex = fileName.indexOf(")");

        if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
            String extractedNumber = fileName.substring(startIndex + 1, endIndex);
            return Integer.parseInt(extractedNumber);
        }
        return -1;
    }

    /**
     * Returns a modified file name without any content within parentheses.
     * If the file name contains parentheses, it removes the content within them,
     * along with the parentheses, and returns the modified file name.
     *
     * @param fileName The original file name to be processed.
     * @return A modified file name without content within parentheses.
     */
    private String fileNameWithoutParenthesis(String fileName){
        String newName = fileName;

        int startIndex = newName.indexOf('(');

        if (startIndex != -1)
             newName = newName.substring(0, startIndex);

        return newName;
    }

    /**
     * Edit content from a file
     * @param elementToEdit
     * @throws PersonalFileSystemException
     */
    public void editElement(FileSystemItem elementToEdit) throws PersonalFileSystemException {

        Position<FileSystemItem> element = findPosition(elementToEdit);

        if(element == null)
            throw new PersonalFileSystemException();
        if(element.element().isDirectory())
            throw new PersonalFileSystemException("It must be a file");
        if(element.element().isLocked())
            throw new PersonalFileSystemException("It must be unlocked");

        element.element().setLastModificationDate();

        notifyObservers(element);
    }

    /**
     *  Search through array of Positions and return the
     *  position with the element provided
     * @param element
     * @return Position or null if not found
     */
    public Position<FileSystemItem> findPosition(FileSystemItem element){
        for (Position position : getFileSystemPositions()) {
            if(position.element().equals(element)) {
                return position;
            }
        }
        return null;
    }

    /**
     * Find the parent of an element
     * @param element
     * @return
     */
    public Position<FileSystemItem> findParent(FileSystemItem element){
       Position<FileSystemItem> currentElement = findPosition(element);
       if(currentElement==null) return null;
       if (currentElement.equals(fileSystem.root())){return null;}

        return fileSystem.parent(currentElement);
    }

    /**
     * Export file to PC
     * @param item
     */
    public void export(FileSystemItem item){
        // Constrói o nome do arquivo usando o nome do PersonalFileSystem
        String userHome = System.getProperty("user.home");

        String fileName = item.getName() + "_export.txt";
        Path filePath = Paths.get(userHome, fileName);
   
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toString()))) {
           writer.write(item.getContent());
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erro ao exportar informações para o arquivo " + fileName);
        }
    }

    /**
     * Find the parent of an element
     * @param element
     * @return
     */
    public List<Position<FileSystemItem>> findChildren(FileSystemItem element){
        Position<FileSystemItem> currentElement = findPosition(element);
        return (List<Position<FileSystemItem>>) fileSystem.children(currentElement) ;
    }

    /**
     * Creates and returns a new instance of {@code PersonalFileSystem}.
     * If a saved model exists in the file "Arquivo.dat," it loads and returns that model.
     * Otherwise, it creates a new model with a root directory named "My Files" and some default folders.
     *
     * @return A new or loaded instance of {@code PersonalFileSystem}.
     */
    public static PersonalFileSystem createModel(){

        if(loadFromFile("Arquivo.dat")!=null){
            return loadFromFile("Arquivo.dat");
        }
        else {
            Directory directory = new Directory("My Files");
            PersonalFileSystem model = new PersonalFileSystem(directory);

            //folders
            Directory directoryThisPC = new Directory("This PC");
            Directory directoryDocuments = new Directory("Documents");
            Directory directoryImages = new Directory("Images");

            model.addElements(directory,
                    directoryThisPC,
                    directoryDocuments,
                    directoryImages
            );

            return model;
        }
    }

    /**
     * Saves the specified {@code PersonalFileSystem} item to a file named "Arquivo.dat."
     *
     * @param item The {@code PersonalFileSystem} item to be saved.
     */
    public void saveToFile(PersonalFileSystem item) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("Arquivo.dat"))) {
            oos.writeObject(item);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads a {@code PersonalFileSystem} instance from the file with the given fileName.
     * If the file doesn't exist, returns null.
     *
     * @param fileName The name of the file to load the {@code PersonalFileSystem} from.
     * @return The loaded {@code PersonalFileSystem} instance or null if the file doesn't exist.
     */
    private static PersonalFileSystem loadFromFile(String fileName) {
        if (!new java.io.File(fileName).exists()) {
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            return (PersonalFileSystem) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Returns the underlying file system structure represented by this {@code PersonalFileSystem}.
     *
     * @return The file system structure as a {@code TreeLinked<FileSystemItem>} instance.
     */
    public TreeLinked<FileSystemItem> getFileSystem(){ return fileSystem; }

    /**
     * Sets the underlying file system structure for this {@code PersonalFileSystem}.
     *
     * @param newFileSystem The new file system structure to be set.
     */
    public void setFileSystem(TreeLinked<FileSystemItem> newFileSystem){
        this.fileSystem = newFileSystem;
    }

    /**
     * Returns a list of positions representing the file system structure.
     *
     * @return A list of positions in the file system as {@code List<Position<FileSystemItem>>}.
     */
    public List<Position<FileSystemItem>> getFileSystemPositions(){ return this.fileSystemPositions; }

    /**
     * Sets the list of positions representing the file system structure.
     *
     * @param positions The new list of positions to be set.
     */
    public  void setFileSystemPositions(List<Position<FileSystemItem>> positions){
        this.fileSystemPositions=positions; }

}
