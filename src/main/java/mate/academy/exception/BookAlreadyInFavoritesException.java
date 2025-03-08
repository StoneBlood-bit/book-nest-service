package mate.academy.exception;

public class BookAlreadyInFavoritesException extends RuntimeException {
    public BookAlreadyInFavoritesException(String message) {
        super(message);
    }
}
