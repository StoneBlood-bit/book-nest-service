package mate.academy.service.book;

import jakarta.persistence.criteria.Join;
import java.util.List;
import java.util.Set;
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
import mate.academy.model.User;
import mate.academy.repository.book.BookRepository;
import mate.academy.repository.user.UserRepository;
import mate.academy.security.AuthenticationService;
import mate.academy.service.genre.GenreService;
import mate.academy.service.image.ImageService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private static final String DEFAULT_SORT_FIELD = "releaseYear";
    private static final Sort.Direction DEFAULT_SORT_DIRECTION = Sort.Direction.ASC;
    private static final Logger logger = LogManager.getLogger(AuthenticationService.class);
    private final BookRepository bookRepository;
    private final GenreService genreService;
    private final BookMapper bookMapper;
    private final ImageService imageService;
    private final SlugGenerator slugGenerator;
    private final UserRepository userRepository;

    @Override
    public BookResponseDto save(BookRequestDto requestDto, Long userId) {

        Book book = bookMapper.toModel(requestDto);

        Set<Genre> genres = genreService.findByIds(requestDto.getGenreIds());
        book.setGenres(genres);

        User donor = userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("Can't find user with id: " + userId)
        );
        book.setDonor(donor);
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
    public Page<BookResponseDto> findAll(
            List<String> genres, String condition,
            String format, String sort, Pageable pageable
    ) {

        Specification<Book> spec = Specification.where(null);

        if (genres != null && !genres.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> {
                Join<Book, Genre> genreJoin = root.join("genres");
                return genreJoin.get("name").in(genres);
            });
        }

        if (condition != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("condition"), condition));
        }

        if (format != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("format"), format));
        }

        Pageable adjustedPageable = getAdjustedPageable(sort, pageable);
        Page<Book> bookPage = bookRepository.findAll(spec, adjustedPageable);
        return bookPage.map(bookMapper::toDto);
    }

    @Override
    public UpdateBookResponseDto update(Long id, UpdateBookRequestDto requestDto) {
        logger.info("Received condition: {}", requestDto.getCondition());

        Book existingBook = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find book with id: " + id)
        );

        bookMapper.toUpdateModel(requestDto, existingBook);

        if (requestDto.getGenreIds() != null) {
            Set<Genre> genres = genreService.findByIds(requestDto.getGenreIds());
            existingBook.setGenres(genres);
        }

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

    @Override
    public List<String> findAllBookTitles() {
        return bookRepository.findAllBookTitles();
    }

    private Pageable getAdjustedPageable(String sort, Pageable pageable) {
        if (sort != null && !sort.isEmpty()) {
            String[] sortParams = sort.split(":");
            if (sortParams.length == 2) {
                String field = sortParams[0];
                String direction = sortParams[1];
                Sort.Order order = "desc".equalsIgnoreCase(direction)
                        ? Sort.Order.desc(field)
                        : Sort.Order.asc(field);
                return PageRequest.of(
                        pageable.getPageNumber(), pageable.getPageSize(), Sort.by(order)
                );
            }
        }
        return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                Sort.by(DEFAULT_SORT_DIRECTION, DEFAULT_SORT_FIELD));
    }
}
