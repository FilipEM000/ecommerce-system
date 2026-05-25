package exception;

public class InvalidRamFormatException extends RuntimeException {
    public InvalidRamFormatException(String message) {
        super(message);
    }
}
