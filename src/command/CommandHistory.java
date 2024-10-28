package command;

import java.util.Stack;

/**
 * Class CommandHistory
 * Functions in coordenation to Command to create a stack of various Commands in chronological order
 */
public class CommandHistory {
    private final Stack<Command> history = new Stack<>();

    /**
     * Pushes a new Command object into the stack
     * @param c
     */
    public void push(Command c) {
        history.push(c);
    }

    /**
     * Pops the last object out of the stack
     * @return Command
     */
    public Command pop() {
        return history.pop();
    }

    /**
     * Checks if the stack is empty
     * @return boolean
     */
    public boolean isEmpty() { return history.isEmpty(); }
}