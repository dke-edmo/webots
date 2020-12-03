package EDMO.Exceptions;

/**
 * @author Tomasz Darmetko
 */
public class InvalidStructure extends RuntimeException {
    public InvalidStructure(String message) {
        super(message);
    }

    public InvalidStructure(String message, Throwable cause) {
        super(message, cause);
    }
}
