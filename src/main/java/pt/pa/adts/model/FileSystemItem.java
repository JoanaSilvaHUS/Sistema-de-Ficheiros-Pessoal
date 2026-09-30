package pt.pa.adts.model;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.time.LocalDateTime;

/**
 * Represents an item in a file system, such as a file or directory.
 *
 * This class provides a common base for both files and directories in a file system.
 * It includes attributes such as name, creation date, last modification date, change count,
 * type (directory or file), locked status, hidden status, size in bytes, and content (for files).
 *
 * @author 202100316, 201801045, 202100190, 202100198
 */
public  class FileSystemItem implements Serializable {
    private String name;
    private LocalDateTime creationDate;
    private LocalDateTime lastModificationDate;
    private int changeCount;

    /**
     * Enumeration representing the type of file system item.
     */
    public enum Type {
        Directory, File
    }
    private boolean isDirectory;
    private boolean isLocked;
    private boolean isHidden;
    private long sizeInBytes;
    private String content;

    /**
     * Constructs a new file system item with the specified name and type.
     *
     * @param name       The name of the file system item.
     * @param isDirectory True if the item is a directory, false if it's a file.
     */
    public FileSystemItem(String name, boolean isDirectory) {
        this.name = name;
        this.isDirectory = isDirectory;
        this.creationDate = LocalDateTime.now();
        this.sizeInBytes = 0;
        this.lastModificationDate = LocalDateTime.now();
        this.changeCount = 0;
        this.isLocked = false;
        this.isHidden = false;

    }

    /**
     * Constructs a new file system item with additional attributes for files.
     *
     * @param name        The name of the file.
     * @param isDirectory  False, as it represents a file.
     * @param isLocked     The locked status of the file.
     * @param isHidden     The hidden status of the file.
     * @param content      The content of the file.
     */
    public FileSystemItem(String name, boolean isDirectory, boolean isLocked,boolean isHidden,String content) {
        this.name = name + ".txt";
        this.isDirectory = isDirectory;
        this.content=content;
        this.sizeInBytes= getSizeInBytesForFile();
        this.creationDate = LocalDateTime.now();
        this.lastModificationDate = LocalDateTime.now();
        this.changeCount = 0;
        this.isLocked = isLocked;
        this.isHidden = isHidden;
    }

    /**
     * Returns the name of the file or directory.
     *
     * @return The name of the file or directory.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the creation date of the file or directory.
     *
     * @return The creation date as a LocalDateTime object.
     */
    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    /**
     * Returns the last modification date of the file or directory.
     *
     * @return The last modification date as a LocalDateTime object.
     */
    public LocalDateTime getLastModificationDate() {
        return lastModificationDate;
    }

    /**
     * Returns the change count, indicating the number of modifications.
     *
     * @return The change count.
     */
    public int getChangeCount() {
        return changeCount;
    }

    /**
     * Checks if the object represents a directory.
     *
     * @return {@code true} if the object is a directory, {@code false} otherwise.
     */
    public boolean isDirectory() {
        return isDirectory;
    }

    /**
     * Checks if the file or directory is locked.
     *
     * @return {@code true} if the file or directory is locked, {@code false} otherwise.
     */
    public boolean isLocked() {
        return isLocked;
    }

    /**
     * Checks if the file or directory is hidden.
     *
     * @return {@code true} if the file or directory is hidden, {@code false} otherwise.
     */
    public boolean isHidden() {
        return isHidden;
    }

    /**
     * Sets the name of the file or directory and updates the change count.
     *
     * @param name The new name for the file or directory.
     */
    public void setName(String name) {
        this.name = name;
        setChangeCount();
    }

    /**
     * Sets the last modification date to the current date and time.
     */
    public void setLastModificationDate() {
        this.lastModificationDate = LocalDateTime.now();
    }

    /**
     * Increments the change count.
     */
    public void setChangeCount() {
        this.changeCount++;
    }

    /**
     * Sets the creation date to the specified date and time.
     *
     * @param time The new creation date for the file or directory.
     */
    public void setCreationDate(LocalDateTime time){
        this.creationDate = time;
    }

    /**
     * Returns the size of the file or directory in bytes.
     *
     * @return The size in bytes.
     */
    public long getSizeInBytes() {
        return sizeInBytes;
    }

    /**
     * Returns the size of the file content in bytes.
     *
     * @return The size of the file content in bytes.
     */
    public long getSizeInBytesForFile(){
        byte []bytes = content.getBytes(Charset.forName("UTF-8"));
        return sizeInBytes =  bytes.length;
    }

    /**
     * Resets the size of the file or directory to zero.
     */
    public void resetSize(){
        sizeInBytes =0;
    }

    /**
     * Adds the specified size to the current size of the file or directory.
     *
     * @param size The size to add.
     */
    public void addToSize(long size){
        sizeInBytes+=size;
    }


    /**
     * Sets the size of the file or directory to the specified size and updates the last modification date.
     *
     * @param sizeInBytes The new size in bytes.
     */
    public void setSizeInBytes(long sizeInBytes) {
        if(content != null) getSizeInBytesForFile();
        else this.sizeInBytes = sizeInBytes;
        setLastModificationDate();
    }

    /**
     * Returns the content of the file.
     *
     * @return The content of the file.
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets the content of the file, updates the last modification date, change count, and size in bytes.
     *
     * @param content The new content for the file.
     */
    public void setContent(String content){
        this.content=content;
        setLastModificationDate();
        setChangeCount();
        setSizeInBytes(content.length());
    }

    /**
     * Sets whether the file or directory is hidden, updates the last modification date, and change count.
     *
     * @param hidden {@code true} if the file or directory is hidden, {@code false} otherwise.
     */
    public void setHidden(boolean hidden) {
        isHidden = hidden;
        setLastModificationDate();
        setChangeCount();
    }

    /**
     * Sets whether the file or directory is locked, updates the last modification date, and change count.
     *
     * @param locked {@code true} if the file or directory is locked, {@code false} otherwise.
     */
    public void setIsLocked(boolean locked){
        isLocked=locked;
        setLastModificationDate();
        setChangeCount();
    }

    /**
     * Returns a string representation of the file system item.
     *
     * @return A string representation of the file system item.
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name);

        return sb.toString();
    }

    /**
     * Returns a detailed string representation of the file system item.
     *
     * @return A detailed string representation of the file system item.
     */
    public String ShowUp(){
        StringBuilder sb = new StringBuilder();
        sb.append("FileSystemItem ").append("\n")
                .append("\n")
                .append(" creationDate: ").append(creationDate).append("\n")
                .append(" lastModificationDate: ").append(lastModificationDate).append("\n")
                .append(" changeCount: ").append(changeCount).append("\n")
                .append(" isDirectory: ").append(isDirectory).append("\n")
                .append(" isLocked: ").append(isLocked).append("\n")
                .append(" isHidden: ").append(isHidden).append("\n")
                .append(" SizeInBytes: ").append(sizeInBytes).append("\n")
                .append(" conteudo: ").append(content);
        return sb.toString();
    }

}
