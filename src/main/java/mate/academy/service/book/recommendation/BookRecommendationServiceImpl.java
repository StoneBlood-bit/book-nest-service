package mate.academy.service.book.recommendation;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import mate.academy.dto.book.BookResponseDto;
import mate.academy.exception.EntityNotFoundException;
import mate.academy.mapper.BookMapper;
import mate.academy.model.Book;
import mate.academy.model.Genre;
import mate.academy.model.User;
import mate.academy.repository.book.BookRepository;
import mate.academy.repository.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BookRecommendationServiceImpl implements BookRecommendationService {
    public static final int MAX_SIZE_OF_LIST = 9;
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public List<BookResponseDto> getRecommendationsForUser(Long userId) {
        User user = userRepository.findByIdWithFavoriteBooks(userId).orElseThrow(
                () -> new EntityNotFoundException("Can't find user with id: " + userId)
        );

        Set<Long> excludedBookIds = user.getFavoriteBooks().stream()
                .map(Book::getId)
                .collect(Collectors.toSet());

        excludedBookIds.addAll(
                user.getDonatedBooks().stream()
                        .map(Book::getId)
                        .collect(Collectors.toSet())
        );

        excludedBookIds.addAll(
                user.getReceivedBooks().stream()
                        .map(Book::getId)
                        .collect(Collectors.toSet())
        );

        Set<String> preferredGenres = user.getFavoriteBooks().stream()
                .flatMap(book -> book.getGenres().stream())
                .map(Genre::getName)
                .collect(Collectors.toSet());

        Set<String> preferredAuthors = user.getFavoriteBooks().stream()
                .map(Book::getAuthor)
                .collect(Collectors.toSet());

        Set<Book> recommendedBooks = bookRepository
                .findBooksByGenreOrAuthor(preferredGenres, preferredAuthors, excludedBookIds);

        if (recommendedBooks.size() < MAX_SIZE_OF_LIST) {
            List<Book> randomBooks = bookRepository
                    .findRandomBooks(MAX_SIZE_OF_LIST - recommendedBooks.size(), excludedBookIds);
            recommendedBooks.addAll(randomBooks);
        }

        return recommendedBooks.stream().limit(MAX_SIZE_OF_LIST)
                .map(bookMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookResponseDto> getRecommendationsForGuest() {
        return bookRepository.findRandomBooksForGuest().stream()
                .map(bookMapper::toDto)
                .collect(Collectors.toList());
    }


}
