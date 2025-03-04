package mate.academy.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import mate.academy.config.MapperConfig;
import mate.academy.dto.book.BookRequestDto;
import mate.academy.dto.book.BookResponseDto;
import mate.academy.dto.book.UpdateBookRequestDto;
import mate.academy.dto.book.UpdateBookResponseDto;
import mate.academy.model.Book;
import mate.academy.model.Genre;
import mate.academy.service.genre.GenreService;
import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface BookMapper {

    @AfterMapping
    default void setCategoryIds(@MappingTarget Book book,
                                BookRequestDto requestDto,
                                @Context GenreService genreService
                                ) {
        System.out.println("AfterMapping is triggered!");
        if (requestDto.getGenreIds() != null) {
            Set<Genre> genres = genreService.findByIds(requestDto.getGenreIds());
            System.out.println("Found genres: " + genres);
            book.setGenres(genres);
        }
    }

    @Named("mapGenresToNames")
    default List<String> mapGenresToNames(Set<Genre> genres) {
        System.out.println("Genres in book: " + genres);
        if (genres == null) {
            return new ArrayList<>();
        }
        return genres.stream()
                .map(Genre::getName)
                .collect(Collectors.toList());
    }

    Book toModel(BookRequestDto requestDto);

    @Mapping(target = "genres", source = "book.genres", qualifiedByName = "mapGenresToNames")
    BookResponseDto toDto(Book book);

    Book toUpdateModel(UpdateBookRequestDto updateBookRequestDto, @MappingTarget Book book);

    @Mapping(target = "genres", source = "book.genres", qualifiedByName = "mapGenresToNames")
    UpdateBookResponseDto toUpdateDto(Book book);
}
