package mate.academy.mapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import mate.academy.config.MapperConfig;
import mate.academy.dto.shoppingcart.ShoppingCartResponseDto;
import mate.academy.model.Genre;
import mate.academy.model.ShoppingCart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface ShoppingCartMapper {
    @Mapping(source = "user.id", target = "userId")
    ShoppingCartResponseDto toDto(ShoppingCart shoppingCart);

    default List<String> map(Set<Genre> genres) {
        return genres.stream()
                .map(Genre::getName)
                .collect(Collectors.toList());
    }
}
