package mate.academy.mapper;

import mate.academy.config.MapperConfig;
import mate.academy.dto.book.BookRequestDto;
import mate.academy.dto.book.BookResponseDto;
import mate.academy.dto.book.UpdateBookRequestDto;
import mate.academy.dto.book.UpdateBookResponseDto;
import mate.academy.exception.EntityNotFoundException;
import mate.academy.model.Book;
import mate.academy.model.Genre;
import mate.academy.repository.genre.GenreRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    @Named("mapGenreToString")
    default String mapGenreToString(Genre genre) {
        return genre != null ? genre.getName() : null;
    }

    @Named("mapGenreIdToGenre")
    default Genre mapGenreIdToGenre(Long genreId, GenreRepository genreRepository) {
        return genreRepository.findById(genreId).orElseThrow(
                () -> new EntityNotFoundException("Can't find genre with id: " + genreId)
        );
    }

    Book toModel(BookRequestDto requestDto);

    @Mapping(source = "genre", target = "genre", qualifiedByName = "mapGenreToString")
    BookResponseDto toDto(Book book);

    Book toUpdateModel(UpdateBookRequestDto updateBookRequestDto, @MappingTarget Book book);

    @Mapping(source = "genre", target = "genre", qualifiedByName = "mapGenreToString")
    UpdateBookResponseDto toUpdateDto(Book book);
}
