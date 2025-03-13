package mate.academy.exception;

public class BookNotInShoppingCartException extends RuntimeException {
    public BookNotInShoppingCartException(String message) {
        super(message);
    }
}
