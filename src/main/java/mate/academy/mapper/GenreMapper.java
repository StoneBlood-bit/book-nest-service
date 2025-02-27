package mate.academy.mapper;

import mate.academy.config.MapperConfig;
import mate.academy.dto.genre.GenreDto;
import mate.academy.model.Genre;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface GenreMapper {
    Genre toModel(GenreDto genreDto);

    GenreDto toDto(Genre genre);
}
