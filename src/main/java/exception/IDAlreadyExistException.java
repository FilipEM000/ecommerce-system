package exception;

public class IDAlreadyExistException extends RuntimeException {
    public IDAlreadyExistException(String message) {
        super(message);
    }
}
