package mate.academy.service.book.recommendation;

import java.util.List;
import mate.academy.dto.book.BookResponseDto;

public interface BookRecommendationService {
    List<BookResponseDto> getRecommendationsForUser(Long userId);
}
