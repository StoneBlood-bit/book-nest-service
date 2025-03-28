package mate.academy.service.book.recommendation;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import mate.academy.dto.book.BookResponseDto;
import mate.academy.mapper.BookMapper;
import mate.academy.model.Book;
import mate.academy.model.Genre;
import mate.academy.model.User;
import mate.academy.repository.book.BookRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookRecommendationServiceImpl implements BookRecommendationService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public List<BookResponseDto> getRecommendationsForUser(User user) {
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


        if (recommendedBooks.size() < 9) {
            List<Book> randomBooks = bookRepository
                    .findRandomBooks(9 - recommendedBooks.size(), excludedBookIds);
            recommendedBooks.addAll(randomBooks);
        }

        return recommendedBooks.stream().limit(9)
                .map(bookMapper::toDto)
                .collect(Collectors.toList());
    }
}
