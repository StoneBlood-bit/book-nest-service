package mate.academy.service.book.donate;

import java.util.List;
import mate.academy.dto.book.BookResponseDto;

public interface DonateBookService {
    List<BookResponseDto> getAllDonatedBooks(Long userId);
}
