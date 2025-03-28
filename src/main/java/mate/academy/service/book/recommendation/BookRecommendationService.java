package mate.academy.service.book.recommendation;

import java.util.List;
import mate.academy.dto.book.BookResponseDto;
import mate.academy.model.User;

public interface BookRecommendationService {
    List<BookResponseDto> getRecommendationsForUser(User user);
}
