package pt.pa.adts.command;

import java.util.Stack;

/**
 * The {@code ActionManager} class is responsible for managing and executing commands
 * in a stack-based manner. It keeps track of executed commands and supports undoing
 * the last executed command.
 *
 * <p>This class is part of the Command design pattern, where it acts as the invoker.
 *
 * @author 202100316, 201801045, 202100190, 202100198
 */
public class ActionManager {
    private Stack<Command> executedCommands;

    /**
     * Constructs a new {@code ActionManager} with an empty stack of executed commands.
     */
    public ActionManager(){
        this.executedCommands = new Stack<>();
    }

    /**
     * Executes a command and adds it to the stack of executed commands.
     *
     * @param command The concrete command to execute.
     */
    public void executeCommand(Command command){
        command.execute();
        executedCommands.push(command);
    }

    /**
     * Undoes the last executed command by calling its {@code unExecute} method.
     * If the stack of executed commands is empty, this method has no effect.
     */
    public void unExecuteCommand(){
        if(!executedCommands.isEmpty()) {
            Command lastCommand = executedCommands.pop();
            lastCommand.unExecute();
        }
    }
}
