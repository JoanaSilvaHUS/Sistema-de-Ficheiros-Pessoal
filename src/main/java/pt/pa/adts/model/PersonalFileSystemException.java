package pt.pa.adts.model;

/**
 * Exception class for representing errors in the PersonalFileSystem.
 * Extends RuntimeException to allow unchecked exceptions.
 * Custom error messages can be provided.
 *
 * @author 202100316, 201801045, 202100190, 202100198
 */
public class PersonalFileSystemException extends RuntimeException{

    /**
     * Constructs a new PersonalFileSystemException with no specified detail message.
     */
    public PersonalFileSystemException(){ }

    /**
     * Constructs a new PersonalFileSystemException with the specified detail message.
     *
     * @param message the detail message (which is saved for later retrieval by the getMessage method).
     */
    public PersonalFileSystemException(String message){
        super(message);
    }

}
