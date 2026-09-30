package pt.pa.adts.command;

/**
 * The {@code Command} interface represents a command in the command pattern.
 * It defines two methods: {@code execute()} and {@code unExecute()}.
 * Classes that implement this interface can be used to encapsulate different
 * operations and support undo functionality.
 *
 * @author 202100316, 201801045, 202100190, 202100198
 */
public interface Command {
    /**
     * Executes the command.
     * This method is responsible for carrying out the specific operation
     * associated with the command.
     */
    void execute();

    /**
     * Unexecutes (undoes) the command.
     * This method is responsible for reversing the effects of the executed command,
     * allowing for an undo functionality.
     */
    void unExecute();
}
