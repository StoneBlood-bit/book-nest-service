package mate.academy.service.genre;

import mate.academy.dto.genre.GenreDto;

public interface GenreService {
    GenreDto save(GenreDto genreDto);

    GenreDto getById(Long id);

    void delete(Long id);
}
