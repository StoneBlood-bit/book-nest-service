package mate.academy.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.dto.book.BookResponseDto;
import mate.academy.model.User;
import mate.academy.service.book.donate.DonateBookService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/donated")
@RequiredArgsConstructor
public class DonateBookController {
    private final DonateBookService donateBookService;

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<BookResponseDto> getAllDonatedBooks(
            @AuthenticationPrincipal User user
    ) {
        return donateBookService.getAllDonatedBooks(user.getId());
    }
}
