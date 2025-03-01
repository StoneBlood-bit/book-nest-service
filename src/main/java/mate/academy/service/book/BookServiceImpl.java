package mate.academy.service.book;

import lombok.RequiredArgsConstructor;
import mate.academy.dto.book.BookRequestDto;
import mate.academy.dto.book.BookResponseDto;
import mate.academy.dto.book.UpdateBookRequestDto;
import mate.academy.dto.book.UpdateBookResponseDto;
import mate.academy.exception.EntityNotFoundException;
import mate.academy.mapper.BookMapper;
import mate.academy.model.Book;
import mate.academy.model.Genre;
import mate.academy.repository.book.BookRepository;
import mate.academy.repository.genre.GenreRepository;
import mate.academy.security.AuthenticationService;
import mate.academy.service.image.ImageService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private static final Logger logger = LogManager.getLogger(AuthenticationService.class);
    private static final int SIZE_OF_PAGE = 12;
    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;
    private final BookMapper bookMapper;
    private final ImageService imageService;

    @Override
    public BookResponseDto save(BookRequestDto requestDto) {
        Genre genre = genreRepository.findById(requestDto.getGenreId()).orElseThrow(
                () -> new EntityNotFoundException("Can't find genre with id: "
                        + requestDto.getGenreId())
        );
        Book book = bookMapper.toModel(requestDto);
        book.setGenre(genre);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public BookResponseDto getById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find book with id: " + id)
        );
        return bookMapper.toDto(book);
    }

    @Override
    public Page<BookResponseDto> findAll(Pageable pageable) {
        Pageable adjustedPageable = PageRequest.of(
                pageable.getPageNumber(), SIZE_OF_PAGE, pageable.getSort()
        );
        Page<Book> bookPage = bookRepository.findAll(adjustedPageable);
        return bookPage.map(bookMapper::toDto);
    }

    @Override
    public UpdateBookResponseDto update(Long id, UpdateBookRequestDto requestDto) {
        logger.info("Received condition: {}", requestDto.getCondition());

        Genre genre = genreRepository.findById(requestDto.getGenreId()).orElseThrow(
                () -> new EntityNotFoundException("Can't find genre with id: "
                        + requestDto.getGenreId())
        );

        // Знайти існуючу книгу
        Book existingBook = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find book with id: " + id)
        );

        // Оновити інформацію про книгу
        bookMapper.toUpdateModel(requestDto, existingBook);

        // Оновити зображення
        existingBook.setImage(imageService.updateImage(requestDto.getFile(),
                existingBook.getImage()));

        return bookMapper.toUpdateDto(bookRepository.save(existingBook));
    }

    @Override
    public void delete(Long id) {
        bookRepository.deleteById(id);
    }
}
