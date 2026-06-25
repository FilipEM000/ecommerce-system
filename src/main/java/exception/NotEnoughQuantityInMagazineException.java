package exception;

public final class NotEnoughQuantityInMagazineException extends RuntimeException {
    public NotEnoughQuantityInMagazineException(String message) {
        super(message);
    }
}
