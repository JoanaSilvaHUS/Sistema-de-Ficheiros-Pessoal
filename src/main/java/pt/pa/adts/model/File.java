package pt.pa.adts.model;

/**
 * Represents a file in a file system.
 *
 * This class extends {@code FileSystemItem} and provides specific functionality
 * for representing files. Each file has a name, a locked status, a hidden status,
 * and content. It overrides the {@code ShowUp} method to provide a custom string
 * representation of the file.
 *
 * @author 202100316, 201801045, 202100190, 202100198
 */
public class File extends FileSystemItem {

    /**
     * Constructs a new file with the specified name, locked status, hidden status,
     * and content.
     *
     * @param name     The name of the file.
     * @param isLocked The locked status of the file.
     * @param isHidden The hidden status of the file.
     * @param content  The content of the file.
     */
public File(String name,boolean isLocked,boolean isHidden,String content){
    super(name,false,isLocked,isHidden,content);
}

    /**
     * Returns a string representation of the file.
     *
     * This method overrides the {@code ShowUp} method in the base class to provide
     * a custom string representation for files.
     *
     * @return A string representation of the file.
     */
    @Override
    public String ShowUp() {
        StringBuilder sb = new StringBuilder();
        sb.append('}');
        return super.toString() + sb.toString();
    }

}
