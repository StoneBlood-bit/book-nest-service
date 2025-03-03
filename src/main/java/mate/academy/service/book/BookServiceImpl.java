package mate.academy.service.book;

import jakarta.persistence.criteria.Join;
import lombok.RequiredArgsConstructor;
import mate.academy.component.SlugGenerator;
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
import org.springframework.data.jpa.domain.Specification;
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
    private final SlugGenerator slugGenerator;

    @Override
    public BookResponseDto save(BookRequestDto requestDto) {
        Genre genre = genreRepository.findById(requestDto.getGenreId()).orElseThrow(
                () -> new EntityNotFoundException("Can't find genre with id: "
                        + requestDto.getGenreId())
        );

        Book book = bookMapper.toModel(requestDto);
        book.setGenre(genre);

        Book savedBook = bookRepository.save(book);

        savedBook.setSlug(slugGenerator.generateSlug(
                savedBook.getAuthor(),
                savedBook.getTitle(),
                savedBook.getId()
        ));
        System.out.println(savedBook.getSlug());

        return bookMapper.toDto(bookRepository.save(savedBook));
    }

    @Override
    public BookResponseDto getById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find book with id: " + id)
        );
        return bookMapper.toDto(book);
    }

    @Override
    public Page<BookResponseDto> findAll(String genre, String condition, Pageable pageable) {
        Pageable adjustedPageable = PageRequest.of(
                pageable.getPageNumber(), SIZE_OF_PAGE, pageable.getSort()
        );

        Specification<Book> spec = Specification.where(null);

        if (genre != null) {
            spec = spec.and((root, query, criteriaBuilder) -> {
                Join<Book, Genre> genreJoin = root.join("genre");
                return criteriaBuilder.equal(genreJoin.get("name"), genre);
            });
        }
        if (condition != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("condition"), condition));
        }

        Page<Book> bookPage = bookRepository.findAll(spec, adjustedPageable);
        return bookPage.map(bookMapper::toDto);
    }

    @Override
    public UpdateBookResponseDto update(Long id, UpdateBookRequestDto requestDto) {
        logger.info("Received condition: {}", requestDto.getCondition());

        Genre genre = genreRepository.findById(requestDto.getGenreId()).orElseThrow(
                () -> new EntityNotFoundException("Can't find genre with id: "
                        + requestDto.getGenreId())
        );

        Book existingBook = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find book with id: " + id)
        );

        bookMapper.toUpdateModel(requestDto, existingBook);

        existingBook.setImage(imageService.updateImage(requestDto.getFile(),
                existingBook.getImage()));

        if (existingBook.getId() == null) {
            existingBook = bookRepository.save(existingBook);
        }

        existingBook.setSlug(slugGenerator.generateSlug(
                existingBook.getAuthor(),
                existingBook.getTitle(),
                existingBook.getId()
        ));

        return bookMapper.toUpdateDto(bookRepository.save(existingBook));
    }

    @Override
    public void delete(Long id) {
        bookRepository.deleteById(id);
    }
}
