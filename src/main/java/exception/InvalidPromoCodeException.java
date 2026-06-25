package exception;

public final class InvalidPromoCodeException extends RuntimeException {
    public InvalidPromoCodeException(String message) {
        super(message);
    }
}
