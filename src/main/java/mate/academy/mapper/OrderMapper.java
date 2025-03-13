package mate.academy.mapper;

import java.util.List;
import java.util.stream.Collectors;
import mate.academy.config.MapperConfig;
import mate.academy.dto.order.OrderResponseDto;
import mate.academy.model.Book;
import mate.academy.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface OrderMapper {
    @Mapping(target = "userEmail", source = "user.email")
    @Mapping(target = "bookTitles", source = "books", qualifiedByName = "mapBookTitles")
    OrderResponseDto toDto(Order order);

    @Named("mapBookTitles")
    default List<String> mapBookTitles(List<Book> books) {
        return books.stream()
                .map(Book::getTitle)
                .collect(Collectors.toList());
    }
}
