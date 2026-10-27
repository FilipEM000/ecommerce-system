package exception;

public class CartDoesNotExistException extends RuntimeException {
    public CartDoesNotExistException(String message) {
        super(message);
    }
}
