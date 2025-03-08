package mate.academy.service.favorite;

import lombok.RequiredArgsConstructor;
import mate.academy.dto.book.AddToFavoriteRequestDto;
import mate.academy.exception.BookAlreadyInFavoritesException;
import mate.academy.exception.BookNotInFavoritesException;
import mate.academy.exception.EntityNotFoundException;
import mate.academy.model.Book;
import mate.academy.model.User;
import mate.academy.repository.book.BookRepository;
import mate.academy.repository.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    @Override
    public void addBookToFavorite(AddToFavoriteRequestDto favoriteRequestDto, Long userId) {
        Book book = bookRepository.findById(favoriteRequestDto.getBookId()).orElseThrow(
                () -> new EntityNotFoundException(
                        "Can't find book with id: " + favoriteRequestDto.getBookId()
                )
        );

        User user = userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("Can't find user with id: " + userId)
        );

        boolean isBookInFavorites = user.getFavoriteBooks().stream()
                .anyMatch(favoriteBook -> favoriteBook.getId().equals(book.getId()));

        if (isBookInFavorites) {
            throw new BookAlreadyInFavoritesException(
                    "The book is already on your favorite list"
            );
        }

        user.getFavoriteBooks().add(book);
        userRepository.save(user);
    }

    @Override
    public void removeBookFromFavorite(Long bookId, Long userId) {
        Book book = bookRepository.findById(bookId).orElseThrow(
                () -> new EntityNotFoundException("Can't find Book with id: " + bookId)
        );

        User user = userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("Can't find user with id: " + userId)
        );

        List<Book> favoriteBooks = user.getFavoriteBooks();

        boolean isBookInFavorites = favoriteBooks.stream()
                .anyMatch(favoriteBook -> favoriteBook.getId().equals(book.getId()));

        if (!isBookInFavorites) {
            throw new BookNotInFavoritesException("The book is not in your favorite list");
        }

        favoriteBooks.removeIf(favoriteBook -> favoriteBook.getId().equals(book.getId()));

        userRepository.save(user);
    }
}
