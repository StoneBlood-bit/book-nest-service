package mate.academy.service.favorite;

import mate.academy.dto.book.AddToFavoriteRequestDto;

public interface FavoriteService {
    void addBookToFavorite(AddToFavoriteRequestDto favoriteRequestDto, Long userId);

    void removeBookFromFavorite(Long bookId, Long userId);
}
