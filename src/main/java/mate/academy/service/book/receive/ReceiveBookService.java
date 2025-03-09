package mate.academy.service.book.receive;

import java.util.List;
import mate.academy.dto.book.BookResponseDto;

public interface ReceiveBookService {
    List<BookResponseDto> getAllReceivedBooks(Long userId);
}
