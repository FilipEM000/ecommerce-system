package exception;

public class InvalidProductTypeException extends RuntimeException {
    public InvalidProductTypeException(String message) {
        super(message);
    }
}
