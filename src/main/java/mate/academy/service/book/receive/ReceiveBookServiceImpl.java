package mate.academy.service.book.receive;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import mate.academy.dto.book.BookResponseDto;
import mate.academy.exception.EntityNotFoundException;
import mate.academy.mapper.BookMapper;
import mate.academy.model.Book;
import mate.academy.model.User;
import mate.academy.repository.user.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReceiveBookServiceImpl implements ReceiveBookService {
    private final BookMapper bookMapper;
    private final UserRepository userRepository;

    @Override
    public List<BookResponseDto> getAllReceivedBooks(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("Can't find user with id: " + userId)
        );

        List<Book> receivedBooks = user.getReceivedBooks();

        return receivedBooks.stream()
                .map(bookMapper::toDto)
                .collect(Collectors.toList());
    }
}
