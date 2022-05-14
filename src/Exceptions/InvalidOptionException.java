package src.Exceptions;
public class InvalidOptionException extends Exception {
    
    /**
     * An exception raised when an invalid option has been chosen by the user
     */
    public InvalidOptionException() {
        super();
    }

    /**
     * Display a message when an InvalidOptionException is raised
     * @param msg the message displayed when this exception is raised
     */
    public InvalidOptionException(String msg) {
        super(msg);
    }

}
