package mate.academy.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.dto.book.BookRequestDto;
import mate.academy.dto.book.BookResponseDto;
import mate.academy.dto.book.UpdateBookRequestDto;
import mate.academy.dto.book.UpdateBookResponseDto;
import mate.academy.service.book.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookResponseDto create(@RequestBody @Valid BookRequestDto requestDto) {
        return bookService.save(requestDto);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BookResponseDto getById(@PathVariable Long id) {
        return bookService.getById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<BookResponseDto> findAll(
            @RequestParam(required = false) List<String> genre,
            @RequestParam(required = false) String condition,
            @RequestParam(required = false) String format,
            @RequestParam(required = false) String sort,
            @PageableDefault(size = 10, sort = "releaseYear", direction = Sort.Direction.ASC)
            Pageable pageable
    ) {
        return bookService.findAll(genre, condition, format, sort, pageable);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UpdateBookResponseDto update(
            @PathVariable Long id,
            @ModelAttribute @Valid UpdateBookRequestDto updateBookRequestDto
    ) {
        return bookService.update(id, updateBookRequestDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        bookService.delete(id);
    }
}
