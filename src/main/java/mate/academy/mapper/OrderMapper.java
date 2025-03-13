package mate.academy.mapper;

import mate.academy.config.MapperConfig;
import mate.academy.dto.order.OrderResponseDto;
import mate.academy.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface OrderMapper {
    @Mapping(target = "userEmail", source = "user.email")
    @Mapping(
            target = "bookTitles",
            expression = "java(order.getBooks().stream().map(Book::getTitle).toList())"
    )
    OrderResponseDto toDto(Order order);
}
