package mate.academy.service.book.favorite;

import java.util.List;
import mate.academy.dto.book.AddToFavoriteRequestDto;
import mate.academy.dto.book.BookResponseDto;

public interface FavoriteService {
    void addBookToFavorite(AddToFavoriteRequestDto favoriteRequestDto, Long userId);

    void removeBookFromFavorite(Long bookId, Long userId);

    List<BookResponseDto> getAllBooksFromFavorite(Long userId);
}
