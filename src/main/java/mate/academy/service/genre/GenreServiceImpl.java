package mate.academy.service.genre;

import lombok.RequiredArgsConstructor;
import mate.academy.dto.genre.GenreDto;
import mate.academy.exception.EntityNotFoundException;
import mate.academy.mapper.GenreMapper;
import mate.academy.model.Genre;
import mate.academy.repository.genre.GenreRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {
    private static final int SIZE_OF_PAGE = 12;
    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;

    @Override
    public GenreDto save(GenreDto genreDto) {
        Genre genre = genreMapper.toModel(genreDto);
        return genreMapper.toDto(genreRepository.save(genre));
    }

    @Override
    public GenreDto getById(Long id) {
        Genre genre = genreRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find the genre with id: " + id)
        );
        return genreMapper.toDto(genre);
    }

    @Override
    public Page<GenreDto> findAll(Pageable pageable) {
        Pageable adjustedPageable = PageRequest.of(
                pageable.getPageNumber(), SIZE_OF_PAGE, pageable.getSort()
        );
        Page<Genre> genresPage = genreRepository.findAll(adjustedPageable);
        return genresPage.map(genreMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        genreRepository.deleteById(id);
    }
}
