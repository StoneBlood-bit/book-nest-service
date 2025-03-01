package mate.academy.service.book;

import mate.academy.dto.book.BookRequestDto;
import mate.academy.dto.book.BookResponseDto;
import mate.academy.dto.book.UpdateBookRequestDto;
import mate.academy.dto.book.UpdateBookResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookResponseDto save(BookRequestDto requestDto);

    BookResponseDto getById(Long id);

    Page<BookResponseDto> findAll(Pageable pageable);

    UpdateBookResponseDto update(Long id, UpdateBookRequestDto requestDto);

    void delete(Long id);
}
