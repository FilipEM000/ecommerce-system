package exception;

public class NotEnoughQuantityInMagazineException extends RuntimeException {
    public NotEnoughQuantityInMagazineException(String message) {
        super(message);
    }
}
