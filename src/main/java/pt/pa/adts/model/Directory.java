package pt.pa.adts.model;


import java.util.ArrayList;
import java.util.List;

/**
 * Represents a directory in the file system.
 *
 * A directory can contain other file system items such as files or subdirectories.
 * It extends the {@code FileSystemItem} class and includes methods for managing its
 * children items.
 *
 * @author 202100316, 201801045, 202100190, 202100198
 */
public class Directory extends FileSystemItem {
    List<FileSystemItem> children;

    /**
     * Constructs a new {@code Directory} with the specified name.
     *
     * @param name The name of the directory.
     */
    public Directory(String name){
        super(name,true);
        children = new ArrayList<>();
    }

    /**
     * Gets the list of children items contained in the directory.
     *
     * @return The list of children items.
     */
    public List<FileSystemItem> getChildren(){
        return children;
    }

    /**
     * Adds a child item to the directory.
     *
     * @param item The file system item to add as a child.
     */
    public void addChildren(FileSystemItem item){
        if(!children.contains(item)){
        children.add(item);
        }
    }

    /**
     * Removes a child item from the directory.
     *
     * @param item The file system item to remove from the children.
     */
    public void removeChildren(FileSystemItem item){

        children.remove(item);
    }

    /**
     * Returns a string representation of the directory.
     *
     * @return A string containing the directory's name and a closing brace.
     */
    public String ShowUp(){
        StringBuilder sb = new StringBuilder();
        sb.append('}');
        return super.toString() + sb.toString();
    }
}
