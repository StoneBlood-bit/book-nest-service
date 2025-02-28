package mate.academy.service.genre;

import mate.academy.dto.genre.GenreDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GenreService {
    GenreDto save(GenreDto genreDto);

    GenreDto getById(Long id);

    Page<GenreDto> findAll(Pageable pageable);

    void delete(Long id);
}
