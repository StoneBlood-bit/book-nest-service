package mate.academy.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.dto.book.BookResponseDto;
import mate.academy.model.User;
import mate.academy.service.book.recommendation.BookRecommendationService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/recommendations")
@RequiredArgsConstructor
public class RecommendationController {
    private final BookRecommendationService bookRecommendationService;

    @PreAuthorize("hasRole('CUSTOMER')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<BookResponseDto> getRecommendationsForUser(@AuthenticationPrincipal User user) {
        return bookRecommendationService.getRecommendationsForUser(user.getId());
    }
}
