package mate.academy.service.genre;

import java.util.List;
import java.util.Set;
import mate.academy.dto.genre.GenreDto;
import mate.academy.model.Genre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GenreService {
    GenreDto save(GenreDto genreDto);

    GenreDto getById(Long id);

    Page<GenreDto> findAll(Pageable pageable);

    void delete(Long id);

    Set<Genre> findByIds(List<Long> ids);
}
