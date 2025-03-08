package mate.academy.exception;

public class BookNotInFavoritesException extends RuntimeException {
    public BookNotInFavoritesException(String message) {
        super(message);
    }
}
