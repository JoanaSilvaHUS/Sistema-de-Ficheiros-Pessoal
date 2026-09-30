package pt.pa.adts.exceptions;

/**
 * The {@code WrongDataInserted} class is a custom runtime exception indicating that
 * the inserted data is not correct during an operation.
 *
 * @author 202100316, 201801045, 202100190, 202100198
 */
public class WrongDataInserted extends RuntimeException{

    /**
     * Constructs a new {@code WrongDataInserted} with a default error message.
     */
    public WrongDataInserted(){super("Inserted Data is not correct");}

    /**
     * Constructs a new {@code WrongDataInserted} with the specified error message.
     *
     * @param message The error message explaining the issue.
     */
    public WrongDataInserted(String message){super(message);}
}
